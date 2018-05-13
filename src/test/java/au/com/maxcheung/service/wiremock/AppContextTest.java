package au.com.maxcheung.service.wiremock;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class AppContextTest {

    private static final String SERVER_PORT_ARG = "server.port=8005";
    private static final String FLYWAY_LOCATION = "flyway.locations=classpath:db/h2";

    @Test
    public void shouldCreatePropertiesMap() {
        assertEquals(1,App.makeMap(new String[] {SERVER_PORT_ARG}).size());
        assertEquals(0,App.makeMap(new String[] {"some random argument"}).size());
    }

    @Ignore
    @Test
	public void shouldRunAppWithServerPort() {
		App.main(new String[] {SERVER_PORT_ARG, FLYWAY_LOCATION});
	}
}