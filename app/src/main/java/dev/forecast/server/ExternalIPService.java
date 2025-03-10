package dev.forecast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.forecast.Util;

public class ExternalIPService {
    protected static final Logger logger = LogManager.getLogger();

    public static final String execute() {
        try {
            String json = Util.httpFetch("https://api.ipify.org?format=json", null);
            //logger.debug("- json: " + json);
            return Util.getJSONField(json, "ip");
        } catch (Exception e) {
            logger.error("could not get external IP: " + e);
            return null;
        }
    }
}