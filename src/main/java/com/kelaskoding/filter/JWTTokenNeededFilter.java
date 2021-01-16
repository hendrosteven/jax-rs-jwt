/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.filter;

import com.kelaskoding.util.KeyGenerator;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Key;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jarvis
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter{
    
    @Inject
    private KeyGenerator keyGenerator;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        System.out.println("JWT: "+ authorizationHeader);
        
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            throw new NotAuthorizedException("Authorization header must be provided with valid token");
        }
        
        String token = authorizationHeader.substring("Bearer".length()).trim();
        
        System.out.println("TOKEN: "+ token);
        
        try{
            Key key = keyGenerator.generateKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        }catch(Exception ex){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
    
}
