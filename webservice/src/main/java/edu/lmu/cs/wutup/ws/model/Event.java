package edu.lmu.cs.wutup.ws.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement
@ToString(includeFieldNames=true,exclude={"comments"})
@EqualsAndHashCode(of={"id"})
@Data
public class Event implements Commentable, Serializable {

    private static final long serialVersionUID = 4439892580209653370L;

    @XmlElement @Getter
    private Integer id;
    @XmlElement @Getter
    private String name;
    @XmlElement @Getter
    private String description;
    @XmlElement @Getter
    private User creator;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
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
}
