package com.fhaachen.swe.freespace.filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Created by thomasgorgels on 09.01.17.
 */
@Provider
public class ExampleExceptionMapper implements ExceptionMapper<Throwable> {
    //private static final Logger log = Logger.getLogger(ExampleExceptionMapper.class.getName() );


    public Response toResponse(Throwable t) {
        if (t instanceof WebApplicationException) {
            return ((WebApplicationException)t).getResponse();
        } else {
            //log.error("Uncaught exception thrown by REST service", t);
            t.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}