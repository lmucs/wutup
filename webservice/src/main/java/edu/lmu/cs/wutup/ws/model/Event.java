package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "event")
public class Event implements Commentable {

    private Integer id;
    private String name;
    private String description;
    private Integer ownerId;
    private ArrayList<Comment> comments;

    public Event() {
        // No-arg constructor required for annotations
    }

    public Event(Integer id, String name, String description, Integer ownerId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public Event(Integer id, String name) {
        this(id, name, null, null);
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setOwnerId(final Integer ownerId) {
        this.ownerId = ownerId;
    }

    @XmlElement(name = "ownerId")
    public Integer getOwnerId() {
        return this.ownerId;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof Event) {
            Event e = Event.class.cast(obj);
            result = Objects.equal(id, e.id) && Objects.equal(e.name, this.name);
        }

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("name", this.name)
                .add("description", this.description)
                .add("ownerId", this.ownerId)
                .toString();
    }

    @Override
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public void removeComment(int commentNumber) {
        this.comments.remove(commentNumber);
    }

    @Override
    public void updateComment(int commentNumber, Comment comment) {
        this.comments.set(commentNumber, comment);
    }

    @Override
    public ArrayList<Comment> getComments() {
        return this.comments;
    }
}
