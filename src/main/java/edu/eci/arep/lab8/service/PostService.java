package edu.eci.arep.lab8.service;

import java.util.ArrayList;
import java.util.List;

import edu.eci.arep.lab8.model.Post;
import edu.eci.arep.lab8.model.Stream;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostService {

    private final Stream stream;

    public PostService() {
        this.stream = new Stream();
    }


    public List<Post> getPosts(){
        System.out.println("Get");
        System.out.println(stream.getPosts().toString());
        return stream.getPosts();
    }

    public Post savePost(Post post){
        System.out.println("Save");
        System.out.println(stream.getPosts().toString());
        return stream.add(post);
    }
}
