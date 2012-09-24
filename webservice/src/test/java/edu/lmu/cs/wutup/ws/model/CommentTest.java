package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTime;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void fieldsSetByConstructorCanBeRead() {
    	DateTime timestamp = new DateTime();
        Comment c = new Comment("This is a comment", timestamp);
        assertThat(c.getBody(), is("This is a comment"));
        assertThat(c.getTimestamp(), is(timestamp));
    }
    
    @Test
    public void fieldsSetBySettersCanBeRead() {
        Comment c = new Comment();
        DateTime timestamp = new DateTime();
        c.setBody("Comments are awesome");
        c.setTimestamp(timestamp);
        assertThat(c.getBody(), is("Comments are awesome"));
        assertThat(c.getTimestamp(), is(timestamp));
    }
}