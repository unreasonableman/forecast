package dev.forecast.client.cmdline;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import dev.forecast.ForecastRequest;
import dev.forecast.ForecastResponse;
import dev.forecast.Util;
import dev.forecast.server.ExternalIPService;

/**
 * A command line client. Invoke with '-h' switch for a list of supported options.
 */
public class CommandLineClient {
    private static void usage(int exitStatus) {
        System.err.println("usage: CommandLineClient [-v] [-zip <zip-code>] [-port <port-number>] [-host <server-ip>]");
        System.err.println("  [-v]                  - retrieve detailed weather info");
        System.err.println("  [-zip <zip_code>]     - get weather info for 5-digit US ZIP code");
        System.err.println("  [-port <port-number>] - specify server port");
        System.err.println("  [-host <server-ip>]   - specify server IP address");

        System.exit(exitStatus);
    }

    /** Main entry point */
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        String command = "forecast";
        String zip = null;
        boolean verbose = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                verbose = true;
                continue;
            }

            if (args[i].equals("-h")) {
                usage(0);
            }

            if (args[i].equals("-zip") && i + 1 < args.length) {
                zip = args[++i];
                continue;
            }

            if (args[i].equals("-host") && i + 1 < args.length) {
                host = args[++i];
                continue;
            }

            if (args[i].equals("-port") && i + 1 < args.length) {
                try{port = Integer.parseInt(args[++i]);}
                catch (NumberFormatException e) {usage(1);}
                continue;
            }

            usage(1);
        }

        try {
            ForecastRequest freq = new ForecastRequest(command, zip, verbose);
            String body = Util.toJSON(freq);
            String url = "http://" + host + ":" + port;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Forwarded-For", ExternalIPService.execute())
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ForecastResponse resp = Util.fromJSON(ForecastResponse.class, response.body());
                System.out.println(resp.format(verbose));
            } else {
                System.err.println("* weather data retrieval failed with status code " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("could not retrieve weather data: " + e);
        }
    }
}