/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.rest;

import com.kelaskoding.filter.JWTTokenNeeded;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author jarvis
 */
@Path("/echo")
@ApplicationScoped
public class EchoController {
    
    @GET
    public Response echo(@QueryParam("msg") String msg){
        Map<String, String> response = new HashMap<>();
        response.put("msg", msg == null ? "no message": msg);
        return Response.ok(response).build();
    }
    
    
    @GET
    @Path("/secure")
    @JWTTokenNeeded
    public Response echoSecure(@QueryParam("msg") String msg){
        Map<String, String> response = new HashMap<>();
        response.put("msg", msg == null ? "no message": msg);
        return Response.ok(response).build();
    }
    
}
