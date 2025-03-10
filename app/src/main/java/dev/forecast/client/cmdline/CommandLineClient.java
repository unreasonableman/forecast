package dev.forecast.client.cmdline;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class CommandLineClient {

    private static void usage() {
        System.err.println("usage: CommandLineClient [-v] [-zip <zip-code>] [-port <port-number>] [-host <server-ip>]");
        System.err.println("  [-v]                  - retrieve detailed weather info");
        System.err.println("  [-zip <zip_code>]     - get weather info for 5-digit US ZIP code");
        System.err.println("  [-port <port-number>] - specify server port");
        System.err.println("  [-host <server-ip>]   - specify server IP address");

        System.exit(1);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        String command = "forecast";
        String zip = null;
        boolean verbose = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i] == "-v") verbose = true;

            if (args[i] == "-zip" && i + 1 < args.length) {
                zip = args[i++];
            }

            if (args[i] == "-host" && i + 1 < args.length) {
                host = args[i++];
            }

            if (args[i] == "-port" && i + 1 < args.length) {
                try{port = Integer.parseInt(args[i++]);}
                catch (NumberFormatException e) {usage();}
            }
        }

        System.out.println("- host: " + host);
        System.out.println("- port: " + port);
        System.out.println("- command: " + command);
        System.out.println("- zip: " + zip);
        System.out.println("- verbose: " + verbose);

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8082")).build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                //return response.body();
            } else {
                //return String.valueOf(response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("could not retrieve weather data: " + e);
        }
    }
}