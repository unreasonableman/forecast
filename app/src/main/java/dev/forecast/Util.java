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

public class Util {
    public static String toJSON(Object object) throws IOException{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    public static <T> T fromJSON(Class<T> clazz, String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(jsonString, clazz);
    }

    public static String getJSONField(String json, String name) {
        Map<String, String> fields = getJSONFields(json, new String[] {name});
        return fields.get(name);
    }

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

