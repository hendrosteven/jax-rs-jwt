package com.kelaskoding.rest;


import com.kelaskoding.dto.LoginData;
import com.kelaskoding.model.User;
import com.kelaskoding.repo.UserRepository;
import com.kelaskoding.util.KeyGenerator;
import com.kelaskoding.util.LoadDatasource;
import com.kelaskoding.util.PasswordUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jarvis
 */
@Path("/users")
@ApplicationScoped
public class UserController {
    
    private UserRepository userRepository;
    
    @Inject
    private KeyGenerator keyGenerator;
    
    @Context
    private UriInfo uriInfo;
    
    public UserController(){
        this.userRepository = new UserRepository(LoadDatasource.getConnection());
    }
    
    @GET
    public Response findAll(){
        List<User> users = userRepository.findAll();
        return Response.ok(users).build();
    }
    
    @POST
    public Response create(User user){
        int affectedRow = userRepository.create(user);
        Map response = new HashMap();
        response.put("affectedRow", affectedRow);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") Long id){
        User user = userRepository.findOne(id);
        return Response.ok(user).build();
    }
    
    @POST
    @Path("/login")
    public Response login(LoginData loginData){
        Map<String, String> response = new HashMap<>();
        try{
            authenticate(loginData.getEmail(), loginData.getPassword());
            
            //generate token
            String token = issueToken(loginData.getEmail());
            response.put("token", token);            
            return Response.ok(response).build();
        }catch(Exception ex){
            response.put("message", ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }
    }
    
    private void authenticate(String email, String password)throws Exception{
        
        User user = userRepository.findByEmailAndPassword(email, PasswordUtils.digestPassword(password));
        if(user == null){
            throw new SecurityException("Invalid user/password");
        }
    }
    
    private String issueToken(String email){
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(5L))) //1 menit
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }
    
    private Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
