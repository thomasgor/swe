package com.fhaachen.swe.freespace.filter;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Benutzer;

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
import java.util.logging.Logger;

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
    private static final Logger log = Logger.getLogger( AuthFilter.class.getName() );



    public void filter(ContainerRequestContext filterContext){
        User user = authenticate(filterContext);
        filterContext.setSecurityContext(new Authorizer(user));
    }

    private User authenticate(ContainerRequestContext filterContext){

        //Extrahieren der login informationen
        String authentication = filterContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        //Ist der String null sind keine Informationen im Header gewesen
        if(authentication == null){
            log.info("Zugriff ohne Authentication im Header");
            String uri = filterContext.getUriInfo().getPath();

            //if(uri.equals("benutzer") || uri.equals("benutzer/") || uri.equals("")){
              //  return new User("","");
            //}
            return new User("","");
        }

        //Wir unterstützen nur Basic Auth, das funktioniert mit Username und Password
        if(!authentication.startsWith("Basic ")){
            log.info("Zugriff mit falscher Authentication im Header, wir unterstuetzen nur Basic");
            return new User("","");
        }

        //User name und Passwort befinden sich nach dem String basic
        //Beispiel : Basic dGhvbWFzOnRob21hcw== verschlüsselt mit BASE64!
        authentication = authentication.substring("Basic ".length());
        @SuppressWarnings("Since15") String decoded = new String(DatatypeConverter.parseBase64Binary(authentication), Charset.forName("ASCII"));
        String[] values = decoded.split(":");

        //Falsche Syntax
        if(values.length < 2){
            return new User("","");
        }

        String userid = values[0];
        String token = values[1];

        //Validieren der Benutzerdaten
        User user;
        if(Benutzer.istBenutzer(userid) && Benutzer.prüfeToken(userid, token)){
            log.info("Benutzer  " + userid + " validiert ");
            user = new User(userid , token);
            return user;
        }else{
            log.info("Benutzer " + userid + " Zugriff verweigert");
            filterContext.abortWith(Antwort.UNAUTHORIZED);
        }

        return (new User("",""));
    }
}
