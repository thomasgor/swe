package com.fhaachen.swe.freespace;


import com.fhaachen.swe.freespace.main.Sitzung;

import javax.ws.rs.core.Response;

/**
 * Die Klasse Antwort enthaelt global auf dem Server genutzte statische Variablen fuer Response-Objekte mit HTTP-Statuscodes
 *
 * @author Thomas Gorgels
 * @version 1.2
 */

public class Antwort {

    //200
    public static final Response OK = Response.status(Response.Status.OK).build();

    //201
    public  static final Response CREATED = Response.status(Response.Status.CREATED).build();

    //400
    public static final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();

    //401
    public static final Response UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();

    //403
    public static final Response FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

    //404
    public static final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();

    //500
    public static final Response INTERNAL_SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    //501
    public static final Response NOT_IMPLEMENTED = Response.status(Response.Status.NOT_IMPLEMENTED).build();

    //900
    public static final Response NO_ACTIVE_SESSION = Response.status(900).entity(Sitzung.getNoActiveSession()).build();

    //901
    public static final Response ROOM_BLOCKED = Response.status(901).build();
}
