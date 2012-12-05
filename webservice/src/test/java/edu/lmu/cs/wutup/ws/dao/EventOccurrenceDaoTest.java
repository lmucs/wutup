package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.NoSuchAttendeeOrOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
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

    private User dondi = new User(1, "john", "dionisio", "dondi@example.com", "dondi", null, "dondifacebookid");
    private User sampleUser = new User(3503, "John", "Lennon", "jlennon@gmail.com", "John", null, null);
    private DateTime sampleDateTime = new DateTime(2012, 10, 31, 23, 56, 0);
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
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(10));
    }

    @Test
    public void findEventOccurrenceByIdWorks() {
        int id = 2;
        EventOccurrence e = eventOccurrenceDao.findEventOccurrenceById(id);
        assertThat(e.getId(), is(id));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void findNonExistentEventOccurrenceIdThrowsException() {
        eventOccurrenceDao.findEventOccurrenceById(1000);
    }

    @Test
    public void findEventOccurrencesByPaginationWorks() {
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(10));
        List<EventOccurrence> eventOccurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, null, null,
                new PaginationData(0, 3));
        assertThat(eventOccurrences.size(), is(3));
        eventOccurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, null, null, new PaginationData(1,
                3));
        assertThat(eventOccurrences.size(), is(3));
        eventOccurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, null, null, new PaginationData(3,
                3));
        assertThat(eventOccurrences.size(), is(1));
    }

    @Test
    public void findEventOccurrencesByCircleWorks() {
        List<EventOccurrence> occurrences = eventOccurrenceDao.findEventOccurrences(null, new Circle(34.1127863,
                -118.3392439, 0.01), null, null, null, new PaginationData(0, 5));
        assertThat(occurrences.size(), is(2));
        EventOccurrence e1 = occurrences.get(0);
        EventOccurrence e2 = occurrences.get(1);
        assertThat(e1.getId(), is(2));
        assertThat(e2.getId(), is(7));
    }

    @Test
    public void findEventOccurrencesByDateTimeIntervalWorks() {
        List<EventOccurrence> occurrences = eventOccurrenceDao.findEventOccurrences(null, null, new Interval(
                new DateTime("2012-01-15T08:30:00"), new DateTime("2012-01-16T11:30:00")), null, null,
                new PaginationData(0, 5));
        assertThat(occurrences.size(), is(1));
        EventOccurrence e = occurrences.get(0);
        assertThat(e.getId(), is(1));
    }

    @Test
    public void findEventOccurencesByVenueIdWorks() {
        List<EventOccurrence> occurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, null, 2,
                new PaginationData(0, 5));
        assertThat(occurrences.size(), is(2));
        EventOccurrence e1 = occurrences.get(0);
        EventOccurrence e2 = occurrences.get(1);
        assertThat(e1.getId(), is(2));
        assertThat(e2.getId(), is(7));
    }

    @Test
    public void findEventOccurencesByOneEventIdWorks() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(2);
        List<EventOccurrence> occurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, a, null,
                new PaginationData(0, 5));
        assertThat(occurrences.size(), is(2));
        assertThat(occurrences.get(0).getId(), is(1));
        assertThat(occurrences.get(1).getId(), is(6));
    }

    @Test
    public void findEventOccurencesByMultipleEventIdsWorks() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        List<EventOccurrence> occurrences = eventOccurrenceDao.findEventOccurrences(null, null, null, a, null,
                new PaginationData(0, 5));
        assertThat(occurrences.size(), is(4));
        assertThat(occurrences.get(0).getId(), is(1));
        assertThat(occurrences.get(1).getId(), is(5));
        assertThat(occurrences.get(2).getId(), is(6));
        assertThat(occurrences.get(3).getId(), is(10));
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
    public void createEventOccurrenceWithoutIdCanBeFound() {
        int newId = eventOccurrenceDao.createEventOccurrence(new EventOccurrence(eventTwo, keck, new DateTime(
                "2012-11-13T08:30:00Z"), new DateTime("2012-11-13T11:30:00Z")));
        EventOccurrence e = eventOccurrenceDao.findEventOccurrenceById(newId);
        assertThat(e.getId(), is(newId));
        assertThat(e.getEvent(), is(eventTwo));
    }

    @Test
    public void createEventOccurrenceWithIdIgnoresId() {
        EventOccurrence e = new EventOccurrence(2, eventOne, keck, new DateTime("2012-11-13T08:30:00Z"), new DateTime(
                "2012-11-13T11:30:00Z"));
        int newId = eventOccurrenceDao.createEventOccurrence(e);
        EventOccurrence eByReturnedId = eventOccurrenceDao.findEventOccurrenceById(newId);
        assertThat(eByReturnedId.getId(), is(newId));
        assertTrue(e.getId().intValue() != 2);
    }

    @Test
    public void updatesToCreatedEventOccurrenceCanBeRead() {
        int newId = eventOccurrenceDao.createEventOccurrence(new EventOccurrence(eventOne, keck, new DateTime(
                "2012-11-13T08:30:00Z"), new DateTime("2012-11-13T11:30:00Z")));
        EventOccurrence e = eventOccurrenceDao.findEventOccurrenceById(newId);
        e.setEvent(eventTwo);
        eventOccurrenceDao.updateEventOccurrence(e);
        e = eventOccurrenceDao.findEventOccurrenceById(newId);
        assertThat(e.getId(), is(newId));
        assertThat(e.getEvent(), is(eventTwo));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void updatingNonExistentEventOccurrenceThrowsException() {
        eventOccurrenceDao.updateEventOccurrence(new EventOccurrence(1000, eventTwo, keck));
    }

    @Test
    public void deletingOccurrenceDecrementsSize() {
        int initialCount = eventOccurrenceDao.findNumberOfEventOccurrences();
        eventOccurrenceDao.deleteEventOccurrence(initialCount - 1);
        assertThat(eventOccurrenceDao.findNumberOfEventOccurrences(), is(initialCount - 1));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void deletingNonExistentOccurrenceThrowsException() {
        eventOccurrenceDao.deleteEventOccurrence(200000);
    }

    @Test
    public void findAttendeesByEventOccurrenceIdWorks() {
        List<User> attendees = eventOccurrenceDao.findAttendeesByEventOccurrenceId(1, new PaginationData(0, 5));
        assertThat(attendees.size(), is(2));
        assertThat(attendees.get(0).getId(), is(1));
        assertThat(attendees.get(1).getId(), is(2));
    }

    @Test
    public void findAttendeesByPaginationWorks() {
        List<User> attendees = eventOccurrenceDao.findAttendeesByEventOccurrenceId(1, new PaginationData(0, 2));
        assertThat(attendees.size(), is(2));
        assertThat(attendees.get(1).getId(), is(2));
    }

    @Test
    public void registerExistingUserAsAttendeeCanBeFound() {
        eventOccurrenceDao.registerAttendeeForEventOccurrence(2, 2);
        List<User> attendees = eventOccurrenceDao.findAttendeesByEventOccurrenceId(1, new PaginationData(0, 2));
        assertThat(attendees.size(), is(2));
        assertThat(attendees.get(1).getId(), is(2));
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void registerNonExistentUserAsAttendeeThrowsException() {
        eventOccurrenceDao.registerAttendeeForEventOccurrence(2, 8675309);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void registerUserAsAttendeeToNonExistentOccurrenceThrowsException() {
        eventOccurrenceDao.registerAttendeeForEventOccurrence(8675309, 2);
    }

    @Test
    public void unregisterAttendeeDecrementsAttendeeSize() {
        eventOccurrenceDao.unregisterAttendeeForEventOccurrence(1, 1);
        List<User> attendees = eventOccurrenceDao.findAttendeesByEventOccurrenceId(1, new PaginationData(0, 1));
        assertThat(attendees.size(), is(1));
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void unregisterNonRegisteredUserFromAttendeesThrowsException() {
        eventOccurrenceDao.unregisterAttendeeForEventOccurrence(1, 3);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void unregisterNonExistentAttendeeThrowsException() {
        eventOccurrenceDao.unregisterAttendeeForEventOccurrence(1, 8675309);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void unregisterAttendeeFromNonExistentOccurrenceThrowsException() {
        eventOccurrenceDao.unregisterAttendeeForEventOccurrence(8675309, 1);
    }

    @Test
    public void findMaxKeyValueForOccurrenceCommentsWorks() {
        int maxValue = eventOccurrenceDao.findMaxKeyValueForComments();
        assertThat(maxValue, is(3));
    }

    @Test
    public void findCommentsSortsByPostDate() {
        List<Comment> comments = eventOccurrenceDao.findComments(1, new PaginationData(0, 10));
        long timestamp = comments.get(0).getPostDate().getMillis();
        for (int i = 1; i < comments.size(); i++) {
            long nextTimestamp = comments.get(i).getPostDate().getMillis();
            assertTrue(nextTimestamp >= timestamp);
            timestamp = nextTimestamp;
        }
    }

    @Test
    public void foundCommentsCanBeRead() {
        DateTime knownCommentTime = new DateTime(2012, 4, 18, 0, 0, 0);
        List<Comment> comments = eventOccurrenceDao.findComments(1, new PaginationData(0, 10));
        assertThat(comments.size(), is(2));
        assertThat(comments.get(0).getAuthor(), is(sampleUser));
        assertThat(comments.get(0).getBody(), is("Aww yeah."));
        assertThat(comments.get(0).getPostDate().getMillis(), is(knownCommentTime.getMillis()));
        assertThat(comments.get(0).getId(), is(1));
        assertThat(comments.get(1).getAuthor(), is(sampleUser));
        assertThat(comments.get(1).getBody(), is("Aww no."));
        assertThat(comments.get(1).getPostDate().getMillis(), is(knownCommentTime.getMillis()));
        assertThat(comments.get(1).getId(), is(2));
    }

    @Test
    public void addCommentIncrementsSize() {
        int initialCount = eventOccurrenceDao.findComments(1, new PaginationData(0, 10)).size();
        eventOccurrenceDao.addComment(1, new Comment(null, "AMERICA!", sampleDateTime, sampleUser));
        int afterCount = eventOccurrenceDao.findComments(1, new PaginationData(0, 10)).size();
        assertThat(afterCount, is(initialCount + 1));
    }

    @Test
    public void createdVenueCommentAutoGeneratesId() {
        int maxKeyValue = eventOccurrenceDao.findMaxKeyValueForComments();
        eventOccurrenceDao.addComment(10, new Comment(null, "Boo", sampleDateTime, sampleUser));
        int nextKeyValue = eventOccurrenceDao.findMaxKeyValueForComments();
        assertThat(nextKeyValue, is(maxKeyValue + 1));
    }

    @Test
    public void addedCommentCanBeFound() {
        Comment c = new Comment(null, "tsk tsk", sampleDateTime, sampleUser);
        int maxKeyValue = eventOccurrenceDao.findMaxKeyValueForComments();
        eventOccurrenceDao.addComment(2, c);
        List<Comment> comments = eventOccurrenceDao.findComments(2, new PaginationData(0, 10));
        assertThat(comments.size(), is(1));
        assertThat(comments.get(0).getId(), is(maxKeyValue + 1));
    }

    @Test
    public void deleteCommentDecrementsSize() {
        int initialCount = eventOccurrenceDao.findComments(1, new PaginationData(0, 10)).size();
        eventOccurrenceDao.deleteComment(1, 1);
        int afterCount = eventOccurrenceDao.findComments(1, new PaginationData(0, 10)).size();
        assertThat(afterCount, is(initialCount - 1));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
