package dev.forecast.server;

import org.junit.Test;
import static org.junit.Assert.*;

public class ServerTest {
    @Test
    public void serverHasLogger() {
        ForecastServer classUnderTest = new ForecastServer();
        assertNotNull("server should have a logger", classUnderTest.getLogger());
    }
}
