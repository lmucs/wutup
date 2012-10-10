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
    private User creator;
    private ArrayList<Comment> comments;

    public Event() {
        // No-arg constructor required for annotations
    }

    public Event(Integer id, String name, String description, User creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    public Event(Integer id, String name) {
        this(id, name, null, null);
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setCreator(final User creator) {
        this.creator = creator;
    }

    @XmlElement(name = "ownerId")
    public User getCreator() {
        return this.creator;
    }

    @Override
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public void removeComment(int commentIndex) {
        this.comments.remove(commentIndex);
    }

    @Override
    public void updateComment(int commentIndex, Comment comment) {
        this.comments.set(commentIndex, comment);
    }

    @Override
    public ArrayList<Comment> getComments() {
        return this.comments;
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
            result = Objects.equal(id, e.id);
        }

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("name", this.name)
                .add("description", this.description)
                .add("creator", this.creator)
                .toString();
    }
}
