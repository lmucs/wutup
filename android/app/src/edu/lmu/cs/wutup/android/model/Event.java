package edu.lmu.cs.wutup.android.model;

import java.util.ArrayList;

public class Event {
    
    private int id;
    private String name;
    private String description;
    private User creator;  
    private ArrayList<Comment> comments;

    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }


    
}