package dev.forecast;

import org.junit.Test;
import static org.junit.Assert.*;

public class ForecastResponseTest {
    @Test
    public void requestDefaultConstructorSetsNoFields() {
        ForecastResponse classUnderTest = new ForecastResponse();
        assertNull("unexpected 'description' value", classUnderTest.description);
        assertFalse("unexpected 'cached' value", classUnderTest.cached);
        assertNull("unexpected 'zip' value", classUnderTest.zip);
        assertNull("unexpected 'area' value", classUnderTest.area);
        assertEquals("unexpected 'temp' value", 0, classUnderTest.temp);
        assertEquals("unexpected 'temp_min' value", 0, classUnderTest.temp_min);
        assertEquals("unexpected 'temp_max' value", 0, classUnderTest.temp_max);
    }
}
