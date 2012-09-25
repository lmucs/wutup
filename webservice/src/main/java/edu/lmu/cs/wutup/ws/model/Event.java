package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "event")
public class Event {

    private Integer id;
    private String name;
    private String description;
    // private List<Category> category = new ArrayList<Category>();
    private User owner;
    private List<User> attendees = new ArrayList<User>();
    // private List<EventOccurrence> eventOccurrences = new ArrayList<EventOccurrence>();


    public Event() {
        // No-arg constructor required for annotations
    }

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Event(int id, String name, String description) {

        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    @XmlElement(name = "owner")
    public User getOwner() {
        return this.owner;
    }

	@XmlElement(name = "attendees")
	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}

	// @XmlElement(name = "eventOccurrence")
	// public List<EventOccurrence> getEventOccurrence() {
	//	return eventOccurrence;
	// }

	// public void setEventOccurrence(List<EventOccurrence> eventOccurrence) {
	// 	this.eventOccurrence = eventOccurrence;
	// }
	
	// @XmlElement(name = "category")
	// public List<Category> getCategory() {
	//	return category;
	// }

	// public void setCategory(List<User> category) {
	// 	this.category = category;
	// }
	
	@Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null) {
            Event other = (Event) obj;
            result = Objects.equal(id, other.id) && Objects.equal(name, other.name);
        }

        return result;
    }
}
