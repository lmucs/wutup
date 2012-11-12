package edu.lmu.cs.wutup.ws.model;

import org.joda.time.DateTime;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "comment")
public class Comment {

    private Integer id;
    private User author;
    private String body;
    private DateTime postDate;

    public Comment() {
        // No-arg constructor required for annotations
    }

    public Comment(Integer id, String body, DateTime date, User author) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.postDate = date;
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "author")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @XmlElement(name = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @XmlElement(name = "postdate")
    public DateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(DateTime date) {
        this.postDate = date;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Comment && Objects.equal(id, Comment.class.cast(object).id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("body", this.body)
                .add("owner", this.author)
                .add("postDate", this.postDate)
                .toString();
    }
}
