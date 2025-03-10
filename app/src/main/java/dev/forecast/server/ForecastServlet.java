package dev.forecast.server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import dev.forecast.ForecastRequest;
import dev.forecast.ForecastResponse;
import dev.forecast.Util;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class IpResponse {
    public String ip;
}

public class ForecastServlet extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger();
    private final String WEATHER_REQ = "https://api.openweathermap.org/data/2.5/weather?zip=%s,us&appid=%s&units=imperial";
    private String apiKey = null;

    private static Cache<String, ForecastResponse> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public ForecastServlet() {
        apiKey = System.getenv("OPEN_WEATHER_KEY");

        if (apiKey == null) {
            throw new RuntimeException("open weather api key not founf");
        }
    }

    private String getZipFromExternalIP() {
        try {
            String json = Util.httpFetch("https://api.ipify.org?format=json", null);
            logger.debug("- json: " + json);
            IpResponse ipResponse = Util.fromJSON(IpResponse.class, json);
            logger.debug("- ipResponse.ip: " + ipResponse.ip);
            json = Util.httpFetch("http://ip-api.com/json/" + ipResponse.ip, "");
            logger.debug("- json: " + json);
            String zip = Util.getJSONField(json, "zip");
            logger.debug("- zip: " + zip);
            return zip;
        } catch (Exception e) {
            logger.error("could not get zip from external IP: " + e);
            return null;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // TODO: guard against accidentally/maliciously large POST bodies,
        // e.g. by reading a fixed length buffer using getInputStream()
        String body = "";

        for (;;) {
            String line = req.getReader().readLine();
            if (line == null) break;

            body += line;
        }
        logger.debug("POST body: " + body);

        try {
            ForecastRequest frequest = Util.fromJSON(ForecastRequest.class, body);
            String zip = frequest.zip;
            String json = null;

            if (zip == null) {
                zip = getZipFromExternalIP();
            }

            if (zip == null || zip.length() != 5) {
                logger.error("no zip supplied, unable to determine automatically");
                resp.setStatus(400);
                return;
            }
            logger.debug("- zip: " + zip);

            resp.setContentType("application/json");
            ForecastResponse fresponse = cache.getIfPresent(zip);

            if (fresponse == null) {
                String weatherReq = String.format(WEATHER_REQ, zip, apiKey);
                logger.debug("- weatherReq: " + weatherReq);

                json = Util.httpFetch(weatherReq, null);
                fresponse = Util.parseResponse(json);
                fresponse.zip = zip;
                cache.put(zip, fresponse);
            } else {
                fresponse.cached = true;
            }

            json = Util.toJSON(fresponse);
            resp.getWriter().write(json);
        } catch (Exception e) {
            logger.error("failed to process client request: " + e);
            resp.setStatus(500);
        }
    }
}
