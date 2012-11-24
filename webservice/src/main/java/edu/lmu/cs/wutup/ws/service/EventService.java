package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface EventService extends CommentService {

    int createEvent(Event e);

    void updateEvent(Event e);

    Event findEventById(int id);

    List<Event> findEvents(String name, User owner, List<Category> categories, Circle circle, PaginationData pagination);

    void deleteEvent(int id);
}
