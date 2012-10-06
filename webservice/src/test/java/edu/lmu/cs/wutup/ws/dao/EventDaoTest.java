package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Event;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database.  The database is setup and torn
 * down around each test so that the tests don't affect each other.
 */
public class EventDaoTest {

    private EmbeddedDatabase database;
    private EventDaoJdbcImpl eventDao = new EventDaoJdbcImpl();

    @Before
    public void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        eventDao.jdbcTemplate = new JdbcTemplate(database);
    }

    @Test
    public void creatingIncrementsSize() {
        Event e = new Event(9, "Company Softball Game", "A super fun time", 1);

        int initialCount = eventDao.findNumberOfEvents();
        eventDao.createEvent(e);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        Event e = new Event(1, "Poker Night", "Cards with the guys", 2);

        int initialCount = eventDao.findNumberOfEvents();
        eventDao.deleteEvent(e);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount - 1));
    }

    @Test
    public void createdEventCanBeFound() {
        eventDao.createEvent(new Event(9, "Company Softball Game", "A super fun time", 3));
        Event e = eventDao.findEventById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("Company Softball Game"));
        assertThat(e.getDescription(), is("A super fun time"));
        assertThat(e.getOwnerId(), is(3));
    }

    @Test
    public void updatesToCreatedEventCanBeRead() {
        eventDao.createEvent(new Event(9, "Company Softball Game", "A super fun time", 4));
        Event e = eventDao.findEventById(9);
        e.setName("Cricket Game");
        e.setOwnerId(3503);
        e.setDescription("A really really super fun time!");
        eventDao.updateEvent(e);
        e = eventDao.findEventById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("Cricket Game"));
        assertThat(e.getDescription(), is("A really really super fun time!"));
        assertThat(e.getOwnerId(), is(3503));
    }

    @Test(expected=EventExistsException.class)
    public void creatingDuplicateEventThrowsException() {
        eventDao.createEvent(new Event(1, "Id1WasAlreadyUsed"));
    }

    @Test(expected=NoSuchEventException.class)
    public void updatingNonExistentEventThrowsException() {
        eventDao.updateEvent(new Event(1000, "Unknown"));
    }

    @Test(expected=NoSuchEventException.class)
    public void deletingNonExistentEventThrowsException() {
        eventDao.deleteEvent(new Event(1000, "Unknown"));
    }

    @Test(expected=NoSuchEventException.class)
    public void findingNonExistentEventThrowsException() {
        eventDao.findEventById(1000);
    }

    @Test
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(eventDao.findNumberOfEvents(), is(8));
    }

    @Test
    public void eventAliceIsInInitialDataSet() {
        List<Event> events = eventDao.findEventsByName("Poker Night", 0, 12);
        assertThat(events.size(), is(1));

    }

    @Test
    public void findingByNonexistentNameReturnsEmptyList() {
        List<Event> events = eventDao.findEventsByName("Qwertyuiop", 0, 10);
        assertThat(events.size(), is(0));
    }

    @Test
    public void findingEventsViaPaginationWorks() {
        assertThat(eventDao.findNumberOfEvents(), is(8));
        List<Event> events = eventDao.findAllEvents(0, 3);
        assertThat(events.size(), is(3));
        events = eventDao.findAllEvents(1, 3);
        assertThat(events.size(), is(3));
        events = eventDao.findAllEvents(2, 3);
        assertThat(events.size(), is(2));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
