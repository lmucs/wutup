package edu.lmu.cs.wutup.ws.model;

import org.joda.time.DateTime;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "comment")
public class Comment {
    private Integer id;
    private User author;
    private String body;
    private DateTime timestamp;

    public Comment() {
        // No-arg constructor required for annotations
    }

    public Comment(Integer id, String body, DateTime timestamp, User author) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.timestamp = timestamp;
        this.author = author;
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

    @XmlElement(name = "timestamp")
    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).add("body", this.body).toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null && obj instanceof Comment) {
            Comment other = (Comment) obj;
            result = Objects.equal(id, other.id) && Objects.equal(body, other.body);
        }

        return result;
    }
}
