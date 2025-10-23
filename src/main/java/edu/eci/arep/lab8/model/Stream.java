package edu.eci.arep.lab8.model;

import org.jboss.logging.annotations.Pos;

import java.util.ArrayList;
import java.util.List;

public class Stream {

    public Stream(){}

    private ArrayList<Post> posts = new ArrayList<Post>();

    public Post add(Post post){
        posts.add(post);
        return post;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }


}