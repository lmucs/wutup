package edu.lmu.cs.wutup.ws.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
public class Event implements Commentable, Serializable {

    private Integer id;
    private String name;
    private String description;
    private User creator;
    private List<Comment> comments;

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

    @XmlElement
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @XmlElement
    public User getCreator() {
        return this.creator;
    }

    public void setCreator(final User creator) {
        this.creator = creator;
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
    public List<Comment> getComments() {
        return this.comments;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Event && Objects.equal(id, Event.class.cast(obj).id);
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
