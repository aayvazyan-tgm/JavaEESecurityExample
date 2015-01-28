package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.Servlet;

/**
 * Embeds a Jetty server to host the Servlet.
 * <p/>
 * Author: Ari Michael Ayvazyan
 * Date: 28.01.2015
 */
public class JettyEmbeddedRunner {
    public void startServer(int port) {
        try {
            Server server = new Server();
            ServerConnector c = new ServerConnector(server);
            c.setIdleTimeout(1000);
            c.setAcceptQueueSize(10);
            c.setPort(port);
            c.setHost("localhost");
            ServletContextHandler handler = new ServletContextHandler(server,
                    "/", true, false);
            ServletHolder servletHolder = new ServletHolder(
                    Servlet.class);
            handler.addServlet(servletHolder, "/*");
            server.addConnector(c);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}