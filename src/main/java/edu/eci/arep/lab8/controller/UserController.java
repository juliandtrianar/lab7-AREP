package edu.eci.arep.lab8.controller;


import edu.eci.arep.lab8.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import com.google.gson.Gson;


import edu.eci.arep.lab8.model.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;

@Path("/users")
public class UserController {

    @Inject
    UserService userService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUsers() {
        Gson gson = new Gson();
        List<User> users = userService.getAllUsers();
        String jsonUser = gson.toJson(users);
        return jsonUser;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(User newUser){
        User user = userService.saveUser(newUser);
        return Response.ok(user).build();
    }



}