package dev.forecast;

import org.junit.Test;
import static org.junit.Assert.*;

public class ForecastRequestTest {
    @Test
    public void requestSavesConstructorArgs() {
        ForecastRequest classUnderTest = new ForecastRequest("forecast", "10001", true);
        assertTrue("unexpected 'cmd' value", classUnderTest.cmd.equals("forecast"));
        assertTrue("unexpected 'zip' value", classUnderTest.zip.equals("10001"));
        assertTrue("unexpected 'verbose' value", classUnderTest.verbose);
    }

    @Test
    public void requestDefaultConstructorSetsNoFields() {
        ForecastRequest classUnderTest = new ForecastRequest();
        assertNull("unexpected 'cmd' value", classUnderTest.cmd);
        assertNull("unexpected 'zip' value", classUnderTest.zip);
        assertFalse("unexpected 'verbose' value", classUnderTest.verbose);
    }
}
