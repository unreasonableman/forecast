package dev.forecast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.forecast.Util;

/**
 * Encapsulate a third party service to retrieve a ZIP code from an IP address.
 */
public class IPToZIPService {
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Return the matching ZIP code for an IP address, or 'null' in case of issues
     * @param ip The IPv4 ip address
     */
    public static final String execute(String ip) {
        try {
            String json = Util.httpFetch("http://ip-api.com/json/" + ip, "");
            // logger.debug("- json: " + json);
            String zip = Util.getJSONField(json, "zip");
            //logger.debug("- zip: " + zip);
            return zip;
        } catch (Exception e) {
            logger.error("could not get zip IP: " + e);
            return null;
        }
    }
}