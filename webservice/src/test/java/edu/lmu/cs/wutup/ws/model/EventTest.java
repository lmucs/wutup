package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class EventTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
    	User eventOwner = new User(21, "John", "Bohnam", "jbohnan@gmail.com", "John");
    	List<User> someUsers = new ArrayList<User>();
        List<EventOccurrence> someOccurrences = new ArrayList<EventOccurrence>();
        List<Category> someCategories = new ArrayList<Category>();
    	Event e = new Event(3, "Pool Party", "Party at Brous House", eventOwner, someUsers, someOccurrences, someCategories );
        assertThat(e.getId(), is(3));
        assertThat(e.getName(), is("Pool Party"));
        assertThat(e.getDescription(), is("Party at Brous House"));
        assertThat(e.getOwner(), is(eventOwner));
        assertThat(e.getAttendees(), is(someUsers));
        assertThat(e.getEventOccurrence(), is(someOccurrences));
        assertThat(e.getCategory(), is(someCategories));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Event e = new Event();
        User eventOwner = new User(21, "John", "Bohnam", "jbohnan@gmail.com", "John");
        List<User> someUsers = new ArrayList<User>();
        List<EventOccurrence> someOccurrences = new ArrayList<EventOccurrence>();
        List<Category> someCategories = new ArrayList<Category>();
        e.setId(5);
        e.setName("Programming Contest");
        e.setDescription("Student Programming Contest");
        e.setOwner(eventOwner);
        e.setAttendees(someUsers);
        e.setEventOccurrence(someOccurrences);
        e.setCategory(someCategories);
        assertThat(e.getId(), is(5));
        assertThat(e.getName(), is("Programming Contest"));
        assertThat(e.getDescription(), is("Student Programming Contest"));
        assertThat(e.getOwner(), is(eventOwner));
        assertThat(e.getAttendees(), is(someUsers));
        assertThat(e.getEventOccurrence(), is(someOccurrences));
        assertThat(e.getCategory(), is(someCategories));
    }

    @Test
    public void equalsUsesIdAndNameOnly() {
        assertThat(new Event(7, "Pool Party"), equalTo(new Event(7, "Pool Party")));
        assertThat(new Event(7, "Pool Party"), not(equalTo(new Event(17, "Pool Party"))));
        assertThat(new Event(7, "Pool Party"), not(equalTo(new Event(7, "Target Practice"))));
        assertFalse(new Event(7, "Pool Party").equals("some string"));
        assertFalse(new Event(7, "Pool Party").equals(null));
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Event(7, "Pool Party").hashCode(), is(7));
    }
}
