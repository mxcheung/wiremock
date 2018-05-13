package au.com.maxcheung.service.wiremock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

/**
 * Unit tests for {@link App}.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class AppTest {
    
    /**
     * Test object.
     */
    @InjectMocks
    private App app;

    /**
     * {@link Spy} {@link SpringApplicationBuilder}.
     */
    @Spy
    private SpringApplicationBuilder builder = new SpringApplicationBuilder(App.class);

    /**
     * Ensures the source of the configured application is the {@link App}.
     */
    @Test
    public void shouldConfigureSourceAsApplication() {
        app.configure(builder);
        verify(builder).sources(App.class);
    }

    /**
     * Ensures configuring gives default properties.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void shouldConfigureDefaultProperties() {
        ArgumentCaptor<Map> captor = forClass(Map.class);
        app.configure(builder);
        verify(builder).properties(captor.capture());

        // Check the basics
        assertEquals("Wiremock Service", captor.getValue().get("application.title"));
        assertEquals("1.0.0", captor.getValue().get("application.version"));
        assertEquals("MaxCheung Digital", captor.getValue().get("application.vendor"));
    }

    /**
     * Ensures configuring of the banner.
     */
    @Test
    public void shouldConfigureBanner() {
        Environment envMock = mock(Environment.class);
        String [] profiles = {"testProfile"};
        when(envMock.getActiveProfiles()).thenReturn(profiles);
        
        ArgumentCaptor<Banner> captor = forClass(Banner.class);
        app.configure(builder);
        verify(builder).banner(captor.capture());

        Banner banner = captor.getValue();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        banner.printBanner(envMock, App.class, new PrintStream(output));

        // Check the banner printed out the application name somewhere in there
        assertTrue(output.toString().contains("Wiremock"));
        assertTrue(output.toString().contains("testProfile"));
    }


}
