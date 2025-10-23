package edu.eci.arep.lab8.controller;

import jakarta.ws.rs.Produces;
import java.util.List;
import com.google.gson.Gson;
import edu.eci.arep.lab8.model.Post;
import edu.eci.arep.lab8.service.PostService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/streams")
public class StreamController {

    @Inject
    PostService postService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPosts() {
        List<Post> posts = postService.getPosts();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(posts);
        return jsonArray;
    }
    
}
