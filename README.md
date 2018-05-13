# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###
This repository is a storing wiremock files.

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)



## Folder Structure

Mappings should follow the URL path.


wiremock
         files 
         mappings
                options
                questionnaire
                       sectiondata 
                
"urlPattern":"/questionnaires/sectiondata.*"



### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact


Building The Project
================
---
###Eclipse:
1. Before importing into eclipse execute the `mvn eclipse:eclipse` command to prepare the project to be able to 
be simply imported into the IDE.
2. Within the IDE, select `import existing eclipse project` option and as the eclipse:eclipse command will have
prepared **.project** and **.classpath** files, this will mean minimal setup required.
3. Add the integration-test src and resources to your classpath to ensure your can easily execute integration tests from the IDE.

###Compiling
The project has been designed to be built via `maven` the pom has been configured to be able to execute unit and
integration tests, generate code coverage reports, and code checkstyle testing.

Four Test Profiles have been configured to aid in building the project depending on what you wish to achieve:

1. `unit-tests` will only execute unit tests.
2. `integration-tests` will only execute integration tests.
3. `all-tests` both unit and integration tests are run.
4. `no-tests` no tests run.

The default profile is __all-tests__.

Some example executions are as followed.
* mvn _clean test_ => Creates code coverage report for unit tests.
* mvn _clean verify -P unit-tests_ => Creates code coverage report for unit tests and fails if does not meet coverage ratios.
* mvn _clean verify -P integration-tests_ => Creates code coverage report for unit tests and fails if does not meet coverage ratios. 
* mvn _clean verify -P all-tests_ => Creates code coverage reports for unit and integration tests and fails if does not meet coverage ratios).
* mvn _clean package -P no-tests_ => Does not execute tests and creates the distributable.
* mvn _clean verify -P all-tests checkstyle:check_ => runs all tests, checkstyle and packages.	

Before submitting your code to GIT run __clean verify -P all-tests checkstyle:check__ and build successfully to ensure that all tests and code style checks are completed to ensure Continuous Integration build errors do not arise.

Test Profiles
=============
no-tests - all tests are ignored
all-tests - all tests are executed
unit-tests - only unit tests are executed
integration-tests - only integration tests are executed


Test wiremock mappings prior to check in
===================================
1. Download wiremock standalone http://wiremock.org/docs/running-standalone/
    Note: wiremock-standalone-2.8.0.jar added to .gitignore
2. Copy wiremock jar to directory webapp\WEBINF
3. C:\git\lonsec-dp-wiremock-service\src\main\webapp\WEB-INF\wiremock>java -jar wiremock-standalone-2.8.0.jar --port 8005
	 /$$      /$$ /$$                     /$$      /$$                     /$$
	| $$  /$ | $$|__/                    | $$$    /$$$                    | $$
	| $$ /$$$| $$ /$$  /$$$$$$   /$$$$$$ | $$$$  /$$$$  /$$$$$$   /$$$$$$$| $$   /$$
	| $$/$$ $$ $$| $$ /$$__  $$ /$$__  $$| $$ $$/$$ $$ /$$__  $$ /$$_____/| $$  /$$/
	| $$$$_  $$$$| $$| $$  \__/| $$$$$$$$| $$  $$$| $$| $$  \ $$| $$      | $$$$$$/
	| $$$/ \  $$$| $$| $$      | $$_____/| $$\  $ | $$| $$  | $$| $$      | $$_  $$
	| $$/   \  $$| $$| $$      |  $$$$$$$| $$ \/  | $$|  $$$$$$/|  $$$$$$$| $$ \  $$
	|__/     \__/|__/|__/       \_______/|__/     |__/ \______/  \_______/|__/  \__/
	
	port:                         8080
	enable-browser-proxying:      false
	no-request-journal:           false

4. http://localhost:8005/api/mytest
	{
	    "working": "YES" 
	}
	
http://locahost:8005/__admin/mappings	

http://localhost:8080/__admin/mappings


