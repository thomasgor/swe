package com.fhaachen.swe.freespace.filter;

import com.fhaachen.swe.freespace.main.Benutzer;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

/**
 * Created by thomas on 29.11.2016.
 */
@Provider
public class Authorizer implements SecurityContext {

    private User user;
    private Principal principal;

    public Authorizer(final User user){
        this.user = user;
        this.principal = new Principal() {
            public String getName() {
                return user.userid;
            }
        };
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isUserInRole(String role) {
        String rolle = Benutzer.getRolle(user.userid);

        //Administratoren stehen nicht in der Datenbank, daher hier ausnahme!
        if(role.equals("admin")){
            if(user.userid.equals("admin") && user.token.equals("admin"))
                return true;
        }
        return (role.equals(rolle));
    }

    public boolean isSecure() {
        //"https".equals(uriInfo.get().getRequestUri().getScheme());)
        return true;
    }

    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
