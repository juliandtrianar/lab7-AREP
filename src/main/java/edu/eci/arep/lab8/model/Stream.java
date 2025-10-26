package edu.eci.arep.lab8.model;

import java.util.ArrayList;
import java.util.List;

public class Stream {
    private static ArrayList<Post> posts = new ArrayList<Post>();

    public Stream(){
    }

    public Post add(Post post){
        posts.add(post);
        return post;
    }

    public ArrayList<Post> getPosts() {
        return new ArrayList<>(posts);
    }
}