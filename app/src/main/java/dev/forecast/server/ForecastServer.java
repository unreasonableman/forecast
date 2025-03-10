package dev.forecast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class ForecastServer {
    protected static final Logger logger = LogManager.getLogger();

    public static final void main(String[] arg) {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(ForecastServlet.class, "/*");

        try {
            server.start();
            System.out.println("server started on port " + ((ServerConnector)server.getConnectors()[0]).getLocalPort());
            server.join();
        } catch (Exception e) {
            logger.error("unable to start server: " + e.getMessage());
        }
    }
}
