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

import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database. The database is setup and torn down
 * around each test so that the tests don't affect each other.
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
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        eventDao.jdbcTemplate = new JdbcTemplate(database);
    }

    @Test
    public void creatingIncrementsSize() {
        Event e = new Event(1000, "Company Softball Game", "Fun fun fun", sam);

        int initialCount = eventDao.findNumberOfEvents();
        eventDao.createEvent(e);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        int initialCount = eventDao.findNumberOfEvents();
        eventDao.deleteEvent(7);
        assertThat(eventDao.findNumberOfEvents(), is(initialCount - 1));
    }

    @Test
    public void createdEventCanBeFound() {
        eventDao.createEvent(new Event(9, "Company Softball Game", "", katrina));
        Event e = eventDao.findEventById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("Company Softball Game"));
    }

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

    @Test(expected = NoSuchEventException.class)
    public void updatingNonExistentEventThrowsException() {
        eventDao.updateEvent(new Event(1000, "Unknown", "No Description", katrina));
    }

    @Test(expected = NoSuchEventException.class)
    public void deletingNonExistentEventThrowsException() {
        eventDao.deleteEvent(1000);
    }

    @Test(expected = NoSuchEventException.class)
    public void findingNonExistentEventThrowsException() {
        eventDao.findEventById(1000);
    }

    @Test
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(eventDao.findNumberOfEvents(), is(8));
    }

    // TODO - when general finding is implemented, do tests that find events and events that return an empty
    // list of events.

    @Test
    public void findingEventsViaPaginationWorks() {
        assertThat(eventDao.findNumberOfEvents(), is(8));
        List<Event> events = eventDao.findEvents(new PaginationData(0, 3));
        assertThat(events.size(), is(3));
        events = eventDao.findEvents(new PaginationData(1, 3));
        assertThat(events.size(), is(3));
        events = eventDao.findEvents(new PaginationData(2, 3));
        assertThat(events.size(), is(2));
    }

    @Test
    public void findingCommentsWorks() {
        List<Comment> comments = eventDao.findComments(1, new PaginationData(0, 10));
        assertThat(comments.size(), is(1));
        assertThat(comments.get(0).getId(), is(1));
        assertThat(comments.get(0).getBody(), is("Boo, sux"));
        assertThat(comments.get(0).getPostDate().toString(), is("2012-03-17T00:00:00.000-07:00"));
    }

    @Test
    public void addCommentsIncrementsEventCommentSize() {
        int initialCount = eventDao.findComments(1, new PaginationData(0, 10)).size();
        eventDao.addComment(1, new Comment(null, "Hello", new DateTime(), sam));
        int afterCount = eventDao.findComments(1, new PaginationData(0, 10)).size();
        assertThat(afterCount, is(initialCount + 1));
    }
    
    @Test
    public void deleteCommentDecrementsSize() {
        int initialCount = eventDao.findComments(1, new PaginationData(0, 10)).size();
        eventDao.deleteComment(1, 1);
        int afterCount = eventDao.findComments(1, new PaginationData(0, 10)).size();
        assertThat(afterCount, is(initialCount - 1));
    }

    @Test
    public void addedCommentForEventCanBeFound() {
        eventDao.addComment(1, new Comment(null, "Hello", new DateTime(2012, 11, 11, 12, 34), sam));
        List<Comment> comments = eventDao.findComments(1, new PaginationData(1, 1));
        assertThat(comments.get(0).getBody(), is("Hello"));
        assertThat(comments.get(0).getId(), is(2));
        // Need to check on this. When creating a new DateTime in Java, timezone is set to utc-8:00,
        // when entering something in h2 through pure sql, the timezone is adjsuted for DST to utc-7:00
        assertThat(comments.get(0).getPostDate().toString(), is("2012-11-11T12:34:00.000-08:00"));
    }
    
    @Test
    public void updatesToEventCommentsCanBeRead() {
        Comment initialComment = eventDao.findComments(1, new PaginationData(0, 10)).get(0);
        initialComment.setBody("OLI OLI OXENFREE");
        eventDao.updateComment(1, initialComment);
        Comment updatedComment = eventDao.findComments(1, new PaginationData(0, 10)).get(0);
        assertThat(updatedComment.getBody(), is("OLI OLI OXENFREE"));
        assertThat(updatedComment.getId(), is(1));
        assertThat(updatedComment.getAuthor(), is(initialComment.getAuthor()));
    }
    
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
