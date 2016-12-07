package com.fhaachen.swe.freespace;


import javax.ws.rs.core.Response;

/**
 * Created by thomasgorgels on 02.12.16.
 */
public class Antwort {
    //200
    public static Response OK = Response.status(Response.Status.OK).build();
    //201
    public static Response CREATED = Response.status(Response.Status.CREATED).build();
    //400
    public static Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();
    //401
    public static Response UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();
    //403
    public static Response FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();
    //404
    public static Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();
    //500
    public static Response INTERNAL_SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    //501
    public static Response NOT_INPLEMENTED = Response.status(Response.Status.NOT_IMPLEMENTED).build();

    //900 response Payment Required, weil eigener code unnötig, wir nutzen ja nicht alle
    public static Response NO_ACTIVE_SESSION = Response.status(Response.Status.PAYMENT_REQUIRED).build();

    //910 response Precondition_Failed, weil eigener code unnötig, wir nutzen ja nicht alle
    public static Response ROOM_BLOCKED = Response.status(Response.Status.PRECONDITION_FAILED).build();
    //...
}
