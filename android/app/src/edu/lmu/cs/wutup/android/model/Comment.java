package edu.lmu.cs.wutup.android.model;

import org.joda.time.DateTime;

public class Comment {
    
    private int id;
    private User author;
    private String body;
    private DateTime postDate;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public DateTime getPostDate() {
        return postDate;
    }
    public void setPostDate(DateTime postDate) {
        this.postDate = postDate;
    }
    
}
