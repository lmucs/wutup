package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.joda.time.DateTime;
import org.junit.Test;

public class CommentTest {

    User alice = new User(1, "alice@example.com");
    DateTime timestamp = new DateTime();

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Comment c = new Comment(1, "This is a comment", timestamp, alice);
        assertThat(c.getId(), is(1));
        assertThat(c.getBody(), is("This is a comment"));
        assertThat(c.getTimestamp(), is(timestamp));
        assertThat(c.getAuthor(), is(alice));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Comment c = new Comment();
        c.setId(10);
        c.setBody("Comments are awesome");
        c.setTimestamp(timestamp);
        c.setAuthor(alice);
        assertThat(c.getId(), is(10));
        assertThat(c.getBody(), is("Comments are awesome"));
        assertThat(c.getTimestamp(), is(timestamp));
        assertThat(c.getAuthor(), is(alice));
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Comment(17, "This is a great event.", timestamp, alice).hashCode(), is(17));
    }

    @Test
    public void toStringProducesExpectedString() {
        Comment c = new Comment(3, "This is a great event.", null, null);
        String expected = "Comment{id=3, body=This is a great event., owner=null, timestamp=null}";
        Comment c1 = new Comment(3, "This is a great event.", timestamp, alice);
        String expected1 = "Comment{id=3, body=This is a great event., "
                + "owner=User{id=1, name=null null, email=alice@example.com, nickname=null}, " + "timestamp="
                + timestamp + "}";
        assertEquals(expected, c.toString());
        assertEquals(expected1, c1.toString());
    }

    @Test
    public void equalsUsesIdAndBodyOnly() {
        assertThat(new Comment(7, "This is a great event.", timestamp, alice), equalTo(new Comment(7,
                "This is a great event.", timestamp, alice)));
        assertThat(new Comment(7, "This is a great event.", timestamp, alice), not(equalTo(new Comment(17,
                "This is a great event.", timestamp, alice))));
        assertThat(new Comment(7, "This is a great event.", timestamp, alice), equalTo(new Comment(7,
                "I HAD A TERRIBLE TIME.", timestamp, alice)));
        assertFalse(new Comment(7, "This is a great event.", timestamp, alice).equals("some string"));
        assertFalse(new Comment(7, "This is a great event.", timestamp, alice).equals(null));
    }
}
