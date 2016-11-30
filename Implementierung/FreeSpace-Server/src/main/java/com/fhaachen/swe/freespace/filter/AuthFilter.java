package com.fhaachen.swe.freespace.filter;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;

/**
 * Created by thomas on 29.11.2016.
 */
//@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    private static final String REALM = "Freespace Rest-Api";


    public void filter(ContainerRequestContext filterContext){
        User user = authenticate(filterContext);
        filterContext.setSecurityContext(new Authorizer(user));
    }

    private User authenticate(ContainerRequestContext filterContext){
        //Extrahieren der login informationen
        String authentication = filterContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        //Ist der String null sind keine Informationen im Header gewesen
        if(authentication == null){
            System.out.println("Anfrage ohne Authorization im HTTP-Header");
            return null;
        }

        //Wir unterstützen nur Basic Auth, das funktioniert mit Username und Password
        if(!authentication.startsWith("Basic ")){
            System.out.println("Wir unterstützen nur Basic Auth");
            return null;
        }

        //User name und Passwort befinden sich nach dem String basic
        //Beispiel : Basic dGhvbWFzOnRob21hcw== verschlüsselt mit BASE64!
        authentication = authentication.substring("Basic ".length());
        @SuppressWarnings("Since15") String decoded = new String(DatatypeConverter.parseBase64Binary(authentication), Charset.forName("ASCII"));
        String[] values = decoded.split(":");

        //Falsche Syntax
        if(values.length < 2){
            return null;
        }

        String username = values[0];
        String password = values[1];

        //Validieren der Benutzerdaten
        User user;
        if(username.equals("user") && password.equals("password")){
            user = new User(username , "user");
            System.out.println("Benutzer authentifiziert");
            return user;
        }else{
            System.out.println("Benutzer nicht authentifiziert");
        }

        return (new User("",""));
    }
}
