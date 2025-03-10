package dev.forecast;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/** Class for a few utility functions, predominantly centered around JSON processing. */
public class Util {
    /**
     * Convert a java object to its JSON representation.
     * @param object The object to convert
     * @return The JSON representation of the input as a string
     * @throws IOException e.g. in case of serialization issues
     */
    public static String toJSON(Object object) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    /**
     * Create an instance of a class from its JSON representation.
     * @param clazz The class to instantiate
     * @param json The JSON representation of the instance
     * @return A class instance
     * @throws Exception in case of issues
     */
    public static <T> T fromJSON(Class<T> clazz, String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(json, clazz);
    }

    /**
     * Return the value of a JSON field as a string. In case of multiple matches,
     * return the first match found.
     * @param json A string reperesentation of the JSON input
     * @param name The name of the desired field
     * @return The value of the field, or 'null' if not found
     */
    public static String getJSONField(String json, String name) {
        Map<String, String> fields = getJSONFields(json, new String[] {name});
        return fields.get(name);
    }

    /**
     * Return the values for a list of JSON field as a map. In case of multiple
     * matches, return the first match found for each field.
     * @param json A string reperesentation of the JSON input
     * @param names An array of field names
     * @return A map of fields, or 'null' in case of parsing issues
     */
    public static Map<String, String> getJSONFields(String json, String[] names) {
        try {
            // TODO: this is inefficient for large JSON strings
            final JsonNode node = new ObjectMapper().readTree(json);
            Map<String, String> fields = new HashMap<String, String>();

            for (String n : names) {
                JsonNode value = node.findValue(n);
                if (value != null) {
                    fields.put(n, value.asText());
                }
            }

            return fields;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Parse a ForecastResponse imstance from a JSON string.
     * @param json The string representation of the JSON
     * @return the instance
     */
    public static ForecastResponse parseResponse(String json) {
        ForecastResponse fresponse = new ForecastResponse();
        Map<String, String> fields = Util.getJSONFields(json, ForecastResponse.FIELD_NAMES);

        fresponse.area = fields.get("name");
        fresponse.temp = (int) Float.parseFloat(fields.get("temp"));
        fresponse.description = fields.get("description");
        fresponse.temp_min = (int) Float.parseFloat(fields.get("temp_min"));
        fresponse.temp_max = (int) Float.parseFloat(fields.get("temp_max"));

        return fresponse;
    }

    /**
     * Issue an htto call to a URL and return the response body as a string.
     * @param url The endpoint to contact
     * @param body An optional body string. If non-null, call will be POST
     * @return response bodybstring, or 'null' in case of issues
     * @throws Exception in case of of some connection or formatting issues
     *
     */
    public static String httpFetch(String url, String body) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = null;
        Duration duration = Duration.ofSeconds(5);

        if (body == null) {
            request = HttpRequest.newBuilder().GET().uri(URI.create(url)).timeout(duration).build();
        } else {
            request = HttpRequest.newBuilder().POST(
                    BodyPublishers.ofString(body)).uri(URI.create(url)).timeout(duration).
                    header("Content-Type", "application/json").build();
        }

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return null;
        }
    }
}

