package dev.forecast.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import dev.forecast.ForecastRequest;

public class ForecastServer {
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
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

        cache.put(req.getZip(), req);
        var value = cache.getIfPresent(req.getZip());
        System.out.println("- value: " + value);
        cache.cleanUp();

        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(ForecastServlet.class, "/*");

        try {
            server.start();
            System.out.println("Server started on port " + ((ServerConnector)server.getConnectors()[0]).getLocalPort());
            server.join();
        } catch (Exception e) {
            logger.error("unable to start server: " + e.getMessage());
        }
    }
}
