package dev.forecast;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class UtilTest {
    private static final String PARSE_INPUT = "{\"coord\":{\"lon\":-149.8761,\"lat\":61.2116},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":34.03,\"feels_like\":26.65,\"temp_min\":32.43,\"temp_max\":35.85,\"pressure\":991,\"humidity\":60,\"sea_level\":991,\"grnd_level\":986},\"visibility\":10000,\"wind\":{\"speed\":9.22,\"deg\":30},\"clouds\":{\"all\":0},\"dt\":1741638363,\"sys\":{\"type\":2,\"id\":2000664,\"country\":\"US\",\"sunrise\":1741624266,\"sunset\":1741664905},\"timezone\":-28800,\"id\":0,\"name\":\"Anchorage\",\"cod\":200}";

    @Test
    public void toJsonTest() {
        ForecastRequest req = new ForecastRequest("forecast", "10001", true);
        String expected = "{\"cmd\": \"forecast\", \"zip\": \"10001\", \"verbose\": true}";

        try {
            String result = Util.toJSON(req);
            assertEquals(
                    "unexpected result: " + result,
                    expected.replaceAll("\\s+", ""),
                    result.replaceAll("\\s+", ""));
        } catch (Exception e) {
            assertTrue("unexpected exception: " + e, false);
        }
    }

    @Test
    public void fromJsonTest() {
        ForecastRequest expected = new ForecastRequest("forecast", "10001", true);
        String json = "{\"cmd\": \"forecast\", \"zip\": \"10001\", \"verbose\": true}";

        try {
            ForecastRequest result = Util.fromJSON(ForecastRequest.class, json);
            assertEquals("unexpected 'cmd' value", expected.cmd, result.cmd);
            assertEquals("unexpected 'zip' value", expected.zip, result.zip);
            assertEquals("unexpected 'verbose' value", expected.verbose, result.verbose);
        } catch (Exception e) {
            assertTrue("unexpected exception: " + e, false);
        }
    }

    @Test
    public void getJSONFieldTest() {
        String json = "{\"cmd\": \"forecast\", \"zip\": \"10001\", \"verbose\": true}";
        String value = Util.getJSONField(json, "zip");

        assertEquals("unexpected 'zip' value", "10001", value);
    }

    @Test
    public void getJSONFieldsTest() {
        String json = "{\"cmd\": \"forecast\", \"zip\": \"10001\", \"verbose\": true}";
        Map<String, String> fields = Util.getJSONFields(json, new String[] { "zip", "cmd" });
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("zip", "10001");
        expected.put("cmd", "forecast");

        assertEquals("unexpected values: " + fields, expected, fields);
    }

    @Test
    public void parseResponseTest() {
        ForecastResponse resp = Util.parseResponse(PARSE_INPUT);
        ForecastResponse expected = new ForecastResponse();

        expected.description = "clear sky";
        expected.area = "Anchorage";
        expected.temp = 34;
        expected.temp_min = 32;
        expected.temp_max = 35;

        assertEquals("unexpected 'cmd' value", expected.description, resp.description);
        assertEquals("unexpected 'area' value", expected.area, resp.area);
        assertEquals("unexpected 'temp' value", expected.temp, resp.temp);
        assertEquals("unexpected 'temp_min' value", expected.temp_min, resp.temp_min);
        assertEquals("unexpected 'temp_max' value", expected.temp_max, resp.temp_max);
    }
}
