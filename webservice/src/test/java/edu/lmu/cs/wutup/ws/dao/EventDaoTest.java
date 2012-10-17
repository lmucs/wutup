package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.User;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database.  The database is setup and torn
 * down around each test so that the tests don't affect each other.
 */
public class EventDaoTest {

    private EmbeddedDatabase database;
    private EventDaoJdbcImpl eventDao = new EventDaoJdbcImpl();

    User sam = new User(1, "sam@example.org");
    User marc = new User(2, "marc@example.org");
    User allyson = new User(3, "allyson@example.org");
    User katrina = new User(8, "Katrina", "Sherbina", "ksherbina@gmail.com", "Kat");

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
        Event e = new Event(9, "Company Softball Game",
        		"Lots of fun in the sun hitting balls and catching them",
        		sam);

        int initialCount = eventDao.findNumberOfEvents();
        eventDao.createEvent(e);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount + 1));
    }

    @Ignore
    @Test
    public void deletingDecrementsSize() {
        Event e = new Event(1, "Poker Night");

        int initialCount = eventDao.findNumberOfEvents();
        eventDao.deleteEvent(e);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount - 1));
    }

    @Ignore
    @Test
    public void createdEventCanBeFound() {
        eventDao.createEvent(new Event(9, "Company Softball Game"));
        Event e = eventDao.findEventById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("Company Softball Game"));
    }

    @Ignore
    @Test
    public void updatesToCreatedEventCanBeRead() {
        eventDao.createEvent(new Event(9, "Company Softball Game", "A really really super fun time!", katrina));
        Event e = eventDao.findEventById(9);
        e.setName("Cricket Game");
        e.setDescription("A really really super fun time!");
        eventDao.updateEvent(e);
        e = eventDao.findEventById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("Cricket Game"));
        assertThat(e.getDescription(), is("A really really super fun time!"));
        assertThat(e.getCreator().getId(), is(katrina.getId()));
    }

    @Test(expected=EventExistsException.class)
    public void creatingDuplicateEventThrowsException() {
        eventDao.createEvent(new Event(1, "Id1WasAlreadyUsed", "", sam));
    }

    @Ignore
    @Test(expected=NoSuchEventException.class)
    public void updatingNonExistentEventThrowsException() {
        eventDao.updateEvent(new Event(1000, "Unknown", "No Description", katrina));
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
