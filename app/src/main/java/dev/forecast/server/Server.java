package dev.forecast.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ForecastRequest {
    private String cmd;
    private String zip;
    private boolean verbose;

    public ForecastRequest(String cmd, String zip, boolean verbose) {
        this.cmd = cmd;
        this.zip = zip;
        this.verbose = verbose;
    }

    public String getCmd() {
        return cmd;
    }

    public String getZip() {
        return zip;
    }

    public boolean getVerbose() {
        return verbose;
    }

    public String toString() {
        return "[request cmd: " + cmd + ", zip: " + zip + ", verbose: " + verbose + "]";
    }
}

public class Server {
    protected static final Logger logger = LogManager.getLogger();

    public Logger getLogger() {
        return logger;
    }

    public static final void main(String[] arg) {
        logger.info("I am server. Hear me roar!");
        ObjectMapper objectMapper = new ObjectMapper();

        ForecastRequest req = new ForecastRequest("forecast", "98765", true);
        try {
            String json = objectMapper.writeValueAsString(req);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var cache = CacheBuilder.newBuilder()
                                .maximumSize(1000)
                                .build();

        cache.put(req.getZip(), req);
        var value = cache.getIfPresent(req.getZip());
        System.out.println("- value: " + value);
        cache.cleanUp();
    }
}
