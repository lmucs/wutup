package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventDao;
import edu.lmu.cs.wutup.ws.model.Event;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    EventDao EventDao;

    @Override
    public void createEvent(Event e) {
        EventDao.createEvent(e);
    }

    @Override
    public void updateEvent(Event e) {
        EventDao.updateEvent(e);
    }

    @Override
    public Event findEventById(int id) {
        return EventDao.findEventById(id);
    }

    @Override
    public List<Event> findEvents(String name, int pageNumber, int pageSize) {
        return name == null ? EventDao.findAllEvents(pageNumber, pageSize) : EventDao.findEventsByName(
                name, pageNumber, pageSize);
    }

    @Override
    public void deleteEvent(Event e) {
        EventDao.deleteEvent(e);
    }
}
