package dev.forecast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.forecast.ForecastResponse;
import dev.forecast.Util;

/**
 * Encapsulate a third party service to retrieve weather data for a ZIP code.
 */
public class WeatherService {
    protected static final Logger logger = LogManager.getLogger();
    private static final String WEATHER_REQ = "https://api.openweathermap.org/data/2.5/weather?zip=%s,us&appid=%s&units=imperial";
    private static String apiKey = null;

    /**
     * Return a ForecastResponse fpr a ZIP code, or 'null' in case of issues
     *
     * @param zip The US ZIP code to use
     */
    public static final ForecastResponse execute(String zip) {
        if (apiKey == null) {
            apiKey = System.getenv("OPEN_WEATHER_KEY");

            if (apiKey == null) {
                throw new RuntimeException("open weather api key not found");
            }
        }

        try {
            String weatherReq = String.format(WEATHER_REQ, zip, apiKey);
            //logger.debug("- weatherReq: " + weatherReq);

            String json = Util.httpFetch(weatherReq, null);
            //logger.debug("- json: " + json);
            return Util.parseResponse(json);
        } catch (Exception e) {
            logger.error("could not get zip IP: " + e);
            return null;
        }
    }
}