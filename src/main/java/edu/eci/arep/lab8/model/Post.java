package edu.eci.arep.lab8.model;

public class Post {

    private User owner;
    private String content;

    public Post(){}

    public Post(User owner, String content) {
        this.owner = owner ;
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public String getContent() {
        return content;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
