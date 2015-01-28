package server;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 28.01.2015
 */
public class Main {
    /**
     * This starts the Jetty standalone server
     *
     * @param args The first argument defines the port on which the servlet is hostet
     */
    public static void main(final String[] args) {
        int port = 8080;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                System.err.println("The first argument must be an integer.");
            System.err.println("Defaulting to port 8080");
        }
        System.out.println("Starting Jetty ..");
        new JettyEmbeddedRunner().startServer(port);
        System.out.println("Started Servlet");
    }
}