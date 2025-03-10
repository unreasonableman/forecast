package dev.forecast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.forecast.Util;

/**
 * Encapsulate a third party service to retrieve an external IP. This is mostly needed for
 * situations where the caller is running on a LAN vs a WAN.
 */
public class ExternalIPService {
    protected static final Logger logger = LogManager.getLogger();

    /** Return the caller's external IP, or 'null' in case of problems */
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