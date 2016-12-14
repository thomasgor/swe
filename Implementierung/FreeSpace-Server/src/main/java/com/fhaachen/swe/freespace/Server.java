package com.fhaachen.swe.freespace;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Datenbank;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.javalite.activejdbc.Base;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 */
public class Server {
    private static URI getBaseURI(){
        return UriBuilder.fromUri("http://localhost/").port(8888).build();
    }

    private static String getURL(){
        try{
            String ip = "http://" + InetAddress.getLocalHost().getHostAddress().toString() + ":8888/";
            return ip;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static final URI BASE_URI = getBaseURI();
    public static final String URL = getURL();



    protected static HttpServer startServer() throws IOException{
        System.out.println("Starting grizzly");
        ResourceConfig rc = new ResourceConfig().packages("com.fhaachen.swe.freespace.ressources");


        rc.register(RolesAllowedDynamicFeature.class);

        rc.register(com.fhaachen.swe.freespace.filter.AuthFilter.class);
        rc.register(com.fhaachen.swe.freespace.filter.Authorizer.class);
        rc.register(com.fhaachen.swe.freespace.filter.User.class);

        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    public static void main(String[] args) {
        boolean test = true;
        try {
            HttpServer httpServer = startServer();
            System.out.println("Webservice published to: " + URL);
            System.out.println("Hit enter to Stop");
            System.in.read();
            httpServer.stop();

            if(test == true){
                //run Testsuite
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
