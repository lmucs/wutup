package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

@XmlRootElement(name = "event")
public class Event {

    private Integer id;
    private String name;
    private String description;
    private String category;
    private String location;
    private DateTime start;
    private DateTime end;
    private User owner;
    private List<User> attendees = new ArrayList<User>();
    

    public Event() {
        // No-arg constructor required for annotations
    }

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Event(int id, String name, String description, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
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

    public void setLocation(final String location) {
        this.location = location;
    }

    @XmlElement(name = "location")
    public String getLocation() {
        return this.location;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    @XmlElement(name = "owner")
    public User getOwner() {
        return this.owner;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }

    @XmlElement(name = "category")
    public String getCategory() {
        return this.category;
    }
    
    @XmlElement(name = "start")
    public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	@XmlElement(name = "end")
	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	@XmlElement(name = "attendees")
	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}
	
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

        if (obj != null && obj instanceof Event) {
            Event other = (Event) obj;
            result = other.id == this.id && Objects.equal(other.name, this.name);
        }

        return result;
    }
}
