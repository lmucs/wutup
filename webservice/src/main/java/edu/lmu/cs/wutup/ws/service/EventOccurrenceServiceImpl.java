package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventOccurrenceDao;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;

@Service
@Transactional
public class EventOccurrenceServiceImpl implements EventOccurrenceService {

    @Autowired
    EventOccurrenceDao eventOccurrenceDao;

    @Override
    public void createEventOccurrence(EventOccurrence e) {
        eventOccurrenceDao.createEventOccurrence(e);
    }

    @Override
    public void updateEventOccurrence(EventOccurrence e) {
        eventOccurrenceDao.updateEventOccurrence(e);
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        return eventOccurrenceDao.findEventOccurrenceById(id);
    }

    @Override
    public List<EventOccurrence> findEventOccurrencesByLocation(
            String location, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findEventOccurrencesByLocation(location,
                pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findEventOccurrencesByStartTime(
            DateTime start, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findEventOccurrencesByStartTime(start,
                pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrences(int pageNumber,
            int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrences(pageNumber, pageSize);
    }

    @Override
    public void deleteEventOccurrence(EventOccurrence e) {
        eventOccurrenceDao.deleteEventOccurrence(e);
    }

    @Override
    public int findNumberOfEventOccurrences() {
        return eventOccurrenceDao.findNumberOfEventOccurrences();
    }
}
