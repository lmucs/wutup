package edu.lmu.cs.wutup.ws.model;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

public class Comment {
    private int id;
    private String body;
    private DateTime timestamp;
    private User author;

    public Comment() {
        // No-arg constructor required for annotations
    }

    public Comment(Integer id, String body, DateTime timestamp, User author) {
        this.id = id;
        this.body = body;
        this.timestamp = timestamp;
        this.author = author;
     }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Comment && Objects.equal(id, Comment.class.cast(obj).id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("author", this.author)
                .add("timestamp", this.timestamp)
                .add("body", body)
                .toString();
    }
}
