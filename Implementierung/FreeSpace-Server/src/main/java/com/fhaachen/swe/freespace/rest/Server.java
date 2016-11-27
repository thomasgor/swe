package com.fhaachen.swe.freespace.rest;

import com.fhaachen.swe.freespace.main.Benutzer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.javalite.activejdbc.Base;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 */
public class Server {

    private static URI getBaseURI(){
        return UriBuilder.fromUri("http://localhost/").port(8888).build();
    }

    public static final URI BASE_URI = getBaseURI();

    protected static HttpServer startServer() throws IOException{
        System.out.println("Starting grizzly");
        ResourceConfig rc = new ResourceConfig().packages("com.fhaachen.swe.freespace.rest");
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    public static void main(String[] args) {
        try {
            HttpServer httpServer = startServer();
            System.out.println("Hit enter to Stop");
            System.in.read();
            httpServer.stop();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
