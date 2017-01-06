package com.fhaachen.swe.freespace;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

/**
 * Klasse Server erzeugt und startet einen HTTP-Server um den REST-Service zur Verfuegung stellen zu koennen
 *
 * @author Thomas Gorgels
 * @version 1.0
 */
public class Server {

    /**
     * Erzeugt eine URI fuer den Server auf dem Localhost mit dem Port 8888 und gibt diese zurueck
     *
     * @return Erzeugte URI
     */
    // Attribut zur Speicherung der URI des Servers
    public static final URI BASE_URI = getBaseURI();
    // Attribut zur Speicherung der URL des Servers
    public static final String URL = getURL();
    public static final String PORT = "8888";

    private static URI getBaseURI(){
        return UriBuilder.fromUri("http://"+getIP()+"/").port(Integer.parseInt(PORT)).build();
    }

    /**
     * Erstellt eine URL als String mit der Adresse des Localhost und dem Port 8888
     *
     * @return URL des Localhost als String
     */

    private static String getURL(){
        try{
            String url = "http://" + InetAddress.getLocalHost().getHostAddress().toString() + ":" + PORT + "/";
            return url;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private static String getIP(){
        try{
            String ip =InetAddress.getLocalHost().getHostAddress().toString();
            System.out.println("IP ausgelesen: " + ip);
            return ip;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }



    /**
     * Erzeugt einen Grizzly-HTTP-Server, laedt die vorher definierten Server-Klassen und gibt den Server dann zurueck
     *
     * @return Grizzly-HTTP-Server
     */

    protected static HttpServer startServer() {
        System.out.println("Starting grizzly");
        ResourceConfig rc = new ResourceConfig().packages("com.fhaachen.swe.freespace.ressources");


        rc.register(RolesAllowedDynamicFeature.class);

        rc.register(com.fhaachen.swe.freespace.filter.AuthFilter.class);
        rc.register(com.fhaachen.swe.freespace.filter.Authorizer.class);
        rc.register(com.fhaachen.swe.freespace.filter.User.class);

        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    /**
     *
     */

    public static GoogleHelper startGoogleHelperThread() {
        System.out.println("Starting GoogleHelperThread.");
        GoogleHelper runnable = new GoogleHelper();
        Thread t = new Thread(runnable);
        t.start();
        return runnable;
    }



    /**
     * Startet den Server mit oder ohne Testsuite und beendet diesen mit einem druecken der Eingabe-Taste
     */

    public static void main(String[] args) {
        boolean test = true;
        try {
//            für PushNots
//            GoogleHelper g = startGoogleHelperThread();

            HttpServer httpServer = startServer();
            System.out.println("Webservice published to: " + URL);
            System.out.println("Hit enter to Stop");
            System.in.read();
            httpServer.stop();
//            g.stop();

            if(test == true){
                //run Testsuite
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
