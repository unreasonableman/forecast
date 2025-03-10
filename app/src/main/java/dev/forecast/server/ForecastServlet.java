package dev.forecast.server;

import java.io.IOException;
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

public class ForecastServlet extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger();

    private static Cache<String, ForecastResponse> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build();

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
                String ip = ExternalIPService.execute();
                zip = IPToZIPService.execute(ip);
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
                fresponse = WeatherService.execute(zip);
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
