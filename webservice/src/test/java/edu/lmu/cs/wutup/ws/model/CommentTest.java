package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTime;
import org.junit.Test;

public class CommentTest {

    User alice = new User(1, "alice@example.com");

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        DateTime timestamp = new DateTime();
        Comment c = new Comment(1, "This is a comment", timestamp, alice);
        assertThat(c.getId(), is(1));
        assertThat(c.getBody(), is("This is a comment"));
        assertThat(c.getTimestamp(), is(timestamp));
        assertThat(c.getAuthor(), is(alice));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Comment c = new Comment();
        DateTime timestamp = new DateTime();
        c.setId(10);
        c.setBody("Comments are awesome");
        c.setTimestamp(timestamp);
        c.setAuthor(alice);
        assertThat(c.getId(), is(10));
        assertThat(c.getBody(), is("Comments are awesome"));
        assertThat(c.getTimestamp(), is(timestamp));
        assertThat(c.getAuthor(), is(alice));
    }

    // TODO tests for hashCode, equals, and toString
}
