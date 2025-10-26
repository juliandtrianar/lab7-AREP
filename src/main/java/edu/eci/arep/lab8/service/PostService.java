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

    public List<Post> getPosts(){;
        List<Post> posts = stream.getPosts();
        return posts;
    }

    public Post savePost(Post post){
        Post saved = stream.add(post);
        return saved;
    }
}