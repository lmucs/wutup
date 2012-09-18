package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTime;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Comment c = new Comment("This is a comment", new DateTime());
        assertThat(c.getBody(), is("This is a comment"));
        assertNotNull(c.getTimestamp());
    }
    
    @Test
    public void fieldsSetBySettersCanBeRead() {
        Comment c = new Comment();
        c.setBody("Comments are awesome");
        c.setTimestamp(new DateTime());
        assertThat(c.getBody(), is("Comments are awesome"));
        assertNotNull(c.getTimestamp());
    }
}
