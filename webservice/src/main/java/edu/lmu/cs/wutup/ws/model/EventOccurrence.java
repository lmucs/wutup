package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

@XmlRootElement(name = "eventOccurrence")
public class EventOccurrence implements Commentable {

    private Integer id;
    private Integer eventId;
    private Venue venue;
    private DateTime start;
    private DateTime end;
    private ArrayList<User> attendees;
    private ArrayList<Comment> comments;

    public EventOccurrence() {
        // No-arg constructor required for annotations
    }

    public EventOccurrence(Integer id, Venue venue) {
        this(id, null, venue, new DateTime(), new DateTime().plusDays(1));
    }

    public EventOccurrence(Integer id, Integer eventId, Venue venue, DateTime start, DateTime end) {
        this.id = id;
        this.eventId = eventId;
        this.venue = venue;
        this.start = start;
        this.end = end;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @XmlElement(name = "eventId")
    public int getEventId() {
        return this.eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @XmlElement(name = "venue")
    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @XmlElement(name = "start")
    public DateTime getStart() {
        return this.start;
    }

    public void setStart(final DateTime start) {
        this.start = start;
    }

    @XmlElement(name = "end")
    public DateTime getEnd() {
        return this.end;
    }

    public void setEnd(final DateTime end) {
        this.end = end;
    }

    @XmlElement(name = "attendees")
    public ArrayList<User> getAttendees() {
        return this.attendees;
    }

    public void addAttendee(User attendee) {
        this.attendees.add(attendee);
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
        return obj instanceof EventOccurrence && Objects.equal(id, EventOccurrence.class.cast(obj).id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("eventId", this.eventId)
                .add("location", this.venue)
                .add("start", this.start)
                .add("end", this.end)
                .toString();
    }
}
