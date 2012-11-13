package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database. The database is setup and torn down
 * around each test so that the tests don't affect each other.
 */
public class EventOccurrenceDaoTest {

    private EmbeddedDatabase database;
    private EventOccurrenceDaoJdbcImpl eventOccurrenceDao = new EventOccurrenceDaoJdbcImpl();

    private User dondi = new User(1, "dondi@example.com");
    private Event eventOne = new Event(1, "Party", "A hoedown!", dondi);
    private Event eventTwo = new Event(2, "Party", "Another hoedown!", dondi);

    Venue keck = new Venue(1, "Pantages Theater", "6233 Hollywood Bl, Los Angeles, CA", 34.1019444, -118.3261111, null);
    Venue uhall = new Venue(2, "Hollywood Bowl", "2301 North Highland Ave, Hollywood, CA", 34.1127863, -118.3392439,
            null);

    @Before
    public void setUp() {
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        eventOccurrenceDao.jdbcTemplate = new JdbcTemplate(database);
    }

    @Test
    public void creatingIncrementsSize() {
        EventOccurrence e = new EventOccurrence(2000, eventOne, keck, new DateTime("2012-11-13T08:30:00Z"),
                new DateTime("2012-11-13T09:40:50Z"));

        int initialCount = eventOccurrenceDao.findNumberOfEventOccurrences();
        eventOccurrenceDao.createEventOccurrence(e);
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(initialCount + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        int initialCount = eventOccurrenceDao.findNumberOfEventOccurrences();
        eventOccurrenceDao.deleteEventOccurrence(initialCount - 1);
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(initialCount - 1));
    }

    @Test
    public void createdEventOccurrenceCanBeFound() {
        eventOccurrenceDao.createEventOccurrence(new EventOccurrence(11, eventTwo, keck));
        EventOccurrence e = eventOccurrenceDao.findEventOccurrenceById(11);
        assertThat(e.getId(), is(11));
        assertThat(e.getEvent(), is(eventTwo));
    }

    @Test
    public void updatesToCreatedEventOccurrenceCanBeRead() {
        eventOccurrenceDao.createEventOccurrence(new EventOccurrence(120, eventOne, keck));
        EventOccurrence e = eventOccurrenceDao.findEventOccurrenceById(120);
        e.setEvent(eventTwo);
        eventOccurrenceDao.updateEventOccurrence(e);
        e = eventOccurrenceDao.findEventOccurrenceById(120);
        assertThat(e.getId(), is(120));
        assertThat(e.getEvent(), is(eventTwo));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void updatingNonExistentEventOccurrenceThrowsException() {
        eventOccurrenceDao.updateEventOccurrence(new EventOccurrence(1000, eventTwo, keck));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void deletingNonExistentEventThrowsException() {
        eventOccurrenceDao.deleteEventOccurrence(1000);
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void findingNonExistentEventThrowsException() {
        eventOccurrenceDao.findEventOccurrenceById(1000);
    }

    @Test
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(10));
    }

    // TODO - when general finding is implemented, do tests that find events and events that return an empty
    // list of events.

    @Test
    public void findingEventOccurrencesViaPaginationWorks() {
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(10));
        List<EventOccurrence> eventOccurrences = eventOccurrenceDao.findAllEventOccurrences(new PaginationData(0, 3));
        assertThat(eventOccurrences.size(), is(3));
        eventOccurrences = eventOccurrenceDao.findAllEventOccurrences(new PaginationData(1, 3));
        assertThat(eventOccurrences.size(), is(3));
        eventOccurrences = eventOccurrenceDao.findAllEventOccurrences(new PaginationData(3, 3));
        assertThat(eventOccurrences.size(), is(1));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
