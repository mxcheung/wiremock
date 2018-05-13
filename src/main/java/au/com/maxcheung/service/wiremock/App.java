package au.com.maxcheung.service.wiremock;


import static com.google.common.base.Joiner.on;
import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.google.common.collect.ImmutableMap;

@SpringBootApplication
@EnableAutoConfiguration
public class App extends SpringBootServletInitializer {

    /**
     * Tool used: http://patorjk.com/software/taag/
     */
    private static final String[] BANNER = {"", 
            " __      __.__                  _____                 __    ",
            "/  \\    /  \\__|______   ____   /     \\   ____   ____ |  | __",
            "\\   \\/\\/   /  \\_  __ \\_/ __ \\ /  \\ /  \\ /  _ \\_/ ___\\|  |/ /",
            " \\        /|  ||  | \\/\\  ___//    Y    (  <_> )  \\___|    < ",
            "  \\__/\\  / |__||__|    \\___  >____|__  /\\____/ \\___  >__|_ \\",
            "       \\/                  \\/        \\/            \\/     \\/"
             };

    
    
    private static final String INDENTATION = "    ";

    private static final String DEFAULT_TITLE = "Wiremock Service";
    private static final String DEFAULT_VERSION = "1.0.0";
    private static final String DEFAULT_VENDOR = "MaxCheung Digital";
    private static final String DEFAULT_DESCRIPTION = "Starter Wiremock Service.";

    /**
     * {@inheritDoc}
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // Obtain information where possible from Manifest file.
        final String title = firstNonNull(App.class.getPackage().getImplementationTitle(), DEFAULT_TITLE);
        final String version = firstNonNull(App.class.getPackage().getImplementationVersion(), DEFAULT_VERSION);
        final ImmutableMap.Builder<String, Object> properties = ImmutableMap.<String, Object>builder().put("application.title", title)
                .put("application.version", version).put("application.vendor", DEFAULT_VENDOR).put("application.description", DEFAULT_DESCRIPTION);

        return builder.sources(App.class).properties(properties.build()).web(true).banner((environment, sourceClass, out) -> {
            for (String line : BANNER) {
                out.print(format("\n%s%s", INDENTATION, line));
            }

            out.print(format("\n%s%s : %s (%s)\n\n", INDENTATION, title, version, on(",").join(environment.getActiveProfiles())));
        });
    }

    public static void main(String[] args) {
        final App app = new App();
        Map<String, Object> props = makeMap(args);
        app.run(app.configure(new SpringApplicationBuilder(App.class).properties(props)).build());
    }

    public static Map<String, Object> makeMap(String[] args) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String arg : args) {
            if (arg.contains("=")) {
                // works only if the key doesn't have any '='
                map.put(arg.substring(0, arg.indexOf('=')), arg.substring(arg.indexOf('=') + 1));
            }
        }
        return map;
    }

}
