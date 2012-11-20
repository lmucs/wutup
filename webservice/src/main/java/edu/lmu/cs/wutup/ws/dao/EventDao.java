package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface EventDao extends CommentDao {

    int createEvent(Event e);

    Event findEventById(int id);

    List<Event> findEvents(String name, User owner, List<Category> categories, List<Venue> venues, Circle circle,
            PaginationData pagination);

    void updateEvent(Event e);

    void deleteEvent(int id);

    int findNumberOfEvents();

}
