package com.fhaachen.swe.freespace;


import javax.ws.rs.core.Response;

/**
 * Created by thomasgorgels on 02.12.16.
 */
public class Antwort {


    //400
    public static Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();
    //401
    public static Response UNAUTHORIZED = Response.status(Response.Status.BAD_REQUEST).build();
    //403
    public static Response FORBIDDEN = Response.status(Response.Status.BAD_REQUEST).build();
    //404
    public static Response NOT_FOUND = Response.status(Response.Status.BAD_REQUEST).build();
    //900
    //901
    //...
}
