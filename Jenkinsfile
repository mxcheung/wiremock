#!/usr/bin/env groovy

pipeline {
    agent any

    tools {
        maven 'MVN350'
        jdk 'JDK8'
    }

    options {
        disableConcurrentBuilds()
        timeout(time: 10, unit: 'MINUTES')
    }

    triggers {
        pollSCM('*/1 9-18 * * 1-5')
    }

    environment {
        GIT_USER_EMAIL = "jenkins@maxcheung.com.au"
        GIT_USER_NAME = "Jenkins Pipeline"
        ARTIFACTORY_ID = "MaxCheungArtifactory"
        VERSION_PROPERTIES_DIR = "c:/continuousIntegration/"
        RELEASE_PROPERTIES_DIR = "c:/continuousIntegration/releaseVersionProperties/"
        MVN_SETTINGS = "c:/continuousIntegration/settings.xml"
        EMAIL_LIST = "itdev@maxcheung.com.au"
    }
 
    stages {
        stage('Initialize Project Build') {
            steps {
                echo "Initializing build of => ${JOB_NAME} (#${BUILD_NUMBER})"
                
                script {
                    // Read Maven Pom to obtain identifier.
                    def pomModel = readMavenPom file: 'pom.xml'
                    env.ARTIFACT = pomModel.artifactId
                    
                    // Set Maven profile, all tests should be run (should never upload a bad artifact).
                    env.PROFILE = 'all-tests'

                    // Prepare changelist and revision values to be passed to pom.
                    env.CHANGE_LIST = '-SNAPSHOT'
                    env.REVISION = env.BRANCH_NAME

                    // Get version number defined in application.properties.
                    env.VERSION_PROPERTIES_FILE = "${VERSION_PROPERTIES_DIR}${env.ARTIFACT}.properties"
                    env.RELEASE_PROPERTIES_FILE = "${RELEASE_PROPERTIES_DIR}${env.ARTIFACT}.properties"
                    def props = readProperties file: env.VERSION_PROPERTIES_FILE
                    def nextVersionKey = "next-version" as String
                    
                    if (!props.containsKey(nextVersionKey)) {
                        error("Failing build due to artifact version not setup correctly in ${env.VERSION_PROPERTIES_FILE}!")
                    }
                    
                    def nextVersion = props.get(nextVersionKey)
                    def nextVersionTokens = nextVersion.split("\\.")
                    if (nextVersionTokens.length != 3) {
                        error("Failing build due to incorrect version format for ${nextVersion} in ${env.VERSION_PROPERTIES_FILE}. Must be in the form of N-major.N-minor.N-patch => eg: 1.0.5")
                    }

                    if (env.BRANCH_NAME == 'integration') {
                        // No updates to version in properties on integration build.
                        env.NEXT_VERSION = nextVersion
                        env.REVISION = "${nextVersion}-(#${env.BUILD_NUMBER})"
                    }
                    else if (env.BRANCH_NAME == 'master') {
                        // New production build live version is next and next will increase build minor.
                        def minorNumber = nextVersionTokens[1] as int
                        env.NEXT_VERSION = "${nextVersionTokens[0]}.${minorNumber + 1}.${nextVersionTokens[2]}"
                        env.REVISION = nextVersion

                        // Set changelist to blank as master build is non-snapshot.
                        env.CHANGE_LIST = ''
                    }
                    else {
                        // No updates to version in properties on non master builds.
                        env.NEXT_VERSION = nextVersion

                        env.REVISION = "${nextVersion}-${env.BRANCH_NAME}-(#${env.BUILD_NUMBER})"
                    }
                   
                   // Setup old next version environment variable to be used to update in later stage.
                   env.OLD_NEXT_VERSION = nextVersion

                   // Display environment variables setup in this stage for the remaines stages.
                   echo "-- ENVIRONMENT PROPERTIES SET FOR THIS BUILD --"
                   echo "env.BRANCH_NAME = ${env.BRANCH_NAME}"
                   echo "env.OLD_NEXT_VERSION = ${env.OLD_NEXT_VERSION}"
                   echo "env.NEXT_VERSION = ${env.NEXT_VERSION}"
                   echo "env.CHANGE_LIST = ${env.CHANGE_LIST}"
                   echo "env.REVISION = ${env.REVISION}"
                   echo "env.PROFILE = ${env.PROFILE}"
                   echo "-----------------------------------------------"
                }
            }
        }

        stage('Compilation and Testing') {
            steps {
                echo "Compilation of => ${ARTIFACT} (${BRANCH_NAME}) with maven profile of => ${PROFILE}"
                bat "mvn -s ${env.MVN_SETTINGS} clean package verify -P \"${env.PROFILE}\" -Drevision=\"${env.REVISION}\" -Dchangelist=\"${env.CHANGE_LIST}\" checkstyle:check" 
            }
        }

        stage('Tag Release') {
            when {
                branch 'master'
            }
            steps {
                bat "git config --global user.email \"${env.GIT_USER_EMAIL}\""
                bat "git config --global user.name \"${env.GIT_USER_NAME}\""
                bat "git tag -a ${env.REVISION}_(${BUILD_NUMBER}) -m \"Production release ${new Date()}\""
                bat "git push origin tag ${env.REVISION}_(${BUILD_NUMBER})"
                echo "Created annotated tag on master branch => ${env.REVISION}"
            }
        }

        stage('Upload To Artifactory') {
            steps {

                echo "Commencing upload of artifact to Artifactory..."

                script {
                    def server = Artifactory.server env.ARTIFACTORY_ID
                    def rtMaven = Artifactory.newMavenBuild()
                    rtMaven.tool = 'MVN350'
                    rtMaven.deployer releaseRepo:'libs-release-local', snapshotRepo:'libs-snapshot-local', server: server
                    rtMaven.resolver releaseRepo:'libs-release', snapshotRepo:'libs-snapshot', server: server
                    def buildInfo = Artifactory.newBuildInfo()
                    def goals = "clean install -P no-tests -Drevision=\"${env.REVISION}\" -Dchangelist=\"${env.CHANGE_LIST}\" -DuniqueVersion=false".toString()
                    rtMaven.run pom: 'pom.xml', goals: goals, buildInfo: buildInfo
                    server.publishBuildInfo buildInfo
                }
            }
        }

       stage('Deploy WAR Snapshot to DEV') {
            when {
                branch 'integration'
            }
            steps {
               echo "Executing deployment script"
			   bat "${env.VERSION_PROPERTIES_DIR}deployToDev.bat ${ARTIFACT} ${env.REVISION}"
			}
		}			

        stage('Update versioning') {
            when {
                branch 'master'
            }
            steps {

                echo "Updating version number after production build..."

                script {
                    def versions = new File("${env.VERSION_PROPERTIES_FILE}")
                    def releases = new File("${env.RELEASE_PROPERTIES_FILE}")

                    def findNext = "next-version=${env.OLD_NEXT_VERSION}"
                    def replaceNext = "next-version=${env.NEXT_VERSION}"

                    def versionsText = versions.text
                    versionsText = versionsText.replaceFirst(findNext, replaceNext)

                    versions.write(versionsText)
                    echo "Updated version ${findNext} => ${replaceNext}"
                    
                    def releaseContent = releases.text
                    releases.write(releaseContent + ",${env.NEXT_VERSION}")

                    echo "Appended release version:${env.NEXT_VERSION} to properties file"
                }
            }
        }
    }
    post {
        success {
            emailext (
                subject: "Jenkins Successful Build: Job '${env.JOB_NAME} (#${env.BUILD_NUMBER})'",
                body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} (#${env.BUILD_NUMBER})':</p>
                    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} (#${env.BUILD_NUMBER})</a>&QUOT;</p>""",
                to: "${env.EMAIL_LIST}"
            )
        }

        failure {
            emailext (
                subject: "Jenkins Failed Build: Job '${env.JOB_NAME} (#${env.BUILD_NUMBER})'",
                body: """<p>FAILED: Job '${env.JOB_NAME} (#${env.BUILD_NUMBER})':</p>
                    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} (#${env.BUILD_NUMBER})</a>&QUOT;</p>""",
                to: "${env.EMAIL_LIST}"
            )
        }
    }
}