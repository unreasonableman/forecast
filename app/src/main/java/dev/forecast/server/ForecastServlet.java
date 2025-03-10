package dev.forecast.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.forecast.ForecastRequest;
import dev.forecast.Util;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * {
 * "weather": [
 * {
 * "id": 800,
 * "main": "Clear",
 * "description": "clear sky",
 * "icon": "01n"
 * }
 * ],
 * "main": {
 * "temp": 58.44,
 * "temp_min": 56.98,
 * "temp_max": 60.91,
 * }
 */

public class ForecastServlet extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger();
    private final String WEATHER_REQ = "https://api.openweathermap.org/data/2.5/weather?zip=%s,us&appid=%s&units=imperial";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write("{\"zip\": \"98765\", \"temperature\": 67}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // TODO: guard against accidentally/maliciously large POST bodies,
        // e.g. by reading a fixed length buffer using getInputStream()
        String body = req.getReader().readLine();
        logger.debug("POST body: " + body);

        try {
            ForecastRequest frequest = Util.fromJSON(ForecastRequest.class, body);
            // String zip = "95820"; // TODO: determine ZIP from http request, local IP -> external IP, or frequest.zip


            String apiKey = System.getenv("OPEN_WEATHER_KEY");
            String zip = frequest.zip;

            // TODO: fall back on client IP or local IP -> external IP fo determine ZIP

            if (zip == null || apiKey == null) {
                logger.error("malformed client request or missing API key");
                resp.setStatus(400);
                return;
            }

            String weatherReq = String.format(WEATHER_REQ, frequest.zip, apiKey);
            logger.debug("- weatherReq: " + weatherReq);

            // TODO: issue open weather API call and parse response or report error

            resp.setContentType("application/json");
            resp.getWriter().write("{\"zip\": \"98765\", \"temperature\": 67}");
        } catch (Exception e) {
            logger.error("failed to process client request: " + e);
            resp.setStatus(500);
        }
    }
}
