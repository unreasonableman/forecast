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

/**
 * The main entry point for actual application logic. Receive user reqiuests for weather
 * data, then check if request includes a ZIP. If no ZIP is present, find user's external
 * IP, convert to US ZIP, then query weather service.
 */
public class ForecastServlet extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger();
    private String serverIP = null;

    private static Cache<String, ForecastResponse> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build();

    /** Handle client POST requests */
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
                String ip = req.getHeader("X-Forwarded-For");
                logger.debug("- header ip: " + ip);

                if (ip == null) {
                    // last resort: user did not spcifiy a ZIP,
                    // and client didn't send originating IP,
                    // so we'll use server IP to find a ZIP
                    if (serverIP == null) {
                        serverIP = ExternalIPService.execute();
                        logger.debug("- serverIP: " + serverIP);
                    }

                    ip = serverIP;
                }

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

                if (fresponse == null) {
                    resp.setStatus(400);
                    return;
                }

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
