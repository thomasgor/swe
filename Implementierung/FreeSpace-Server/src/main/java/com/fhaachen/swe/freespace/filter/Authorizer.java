package com.fhaachen.swe.freespace.filter;

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
                return user.username;
            }
        };
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isUserInRole(String role) {
        return (role.equals(user.role));
    }

    public boolean isSecure() {
        //"https".equals(uriInfo.get().getRequestUri().getScheme());)
        System.out.println("Server frag nach isSecure");
        return true;
    }

    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
