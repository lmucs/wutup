package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database. The database is setup and torn down
 * around each test so that the tests don't affect each other.
 */
public class VenueDaoTest {

    private EmbeddedDatabase database;
    private VenueDaoJdbcImpl venueDao = new VenueDaoJdbcImpl();
    private User sampleUser = new User(3503, "John", "Lennon", "jlennon@gmail.com", "John");
    private DateTime sampleDateTime = new DateTime(2012, 10, 31, 23, 56, 0);

    @Before
    public void setUp() {
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        venueDao.jdbcTemplate = new JdbcTemplate(database);
    }

    @Test
    public void creatingIncrementsSize() {
        Venue e = new Venue(1000, "name", "address", 102.2, 90.3, null);

        int initialCount = venueDao.findNumberOfVenues();
        venueDao.createVenue(e);
        assertThat(venueDao.findNumberOfVenues(), is(initialCount + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        int initialCount = venueDao.findNumberOfVenues();
        venueDao.deleteVenue(8);
        assertThat(venueDao.findNumberOfVenues(), is(initialCount - 1));
    }

    @Test
    public void createdVenueWithIdSpecifiedCanBeFound() {
        venueDao.createVenue(new Venue(9, "A venue name", "this is an address"));
        Venue e = venueDao.findVenueById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("A venue name"));
    }

    @Test
    public void createdVenueWithoutIdHasCorrectIdGenerated() {
        Venue v = new Venue(null, "I'm a venue!", "Nowhere, CA, 94041", 26.0000, -118.3432, null);
        venueDao.createVenue(v);
        assertThat(v.getId(), is(11));
    }

    @Test
    public void updatesToCreatedVenueCanBeRead() {
        venueDao.createVenue(new Venue(9, "A venue name", "this is an address"));
        Venue update = new Venue(9, "new name", null);
        venueDao.updateVenue(update);
        Venue v = venueDao.findVenueById(9);
        assertThat(v.getId(), is(9));
        assertThat(v.getName(), is("new name"));
        assertThat(v.getAddress(), is("this is an address"));
    }

    @Test(expected = NoSuchVenueException.class)
    public void updatingNonExistentVenueThrowsException() {
        venueDao.updateVenue(new Venue(1000, "Unknown PLace", "Unknown Address"));
    }

    @Test(expected = NoSuchVenueException.class)
    public void deletingNonExistentVenueThrowsException() {
        venueDao.deleteVenue(1000);
    }

    @Test(expected = NoSuchVenueException.class)
    public void findingNonExistentVenueThrowsException() {
        venueDao.findVenueById(1000);
    }

    @Test
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(venueDao.findNumberOfVenues(), is(9));
    }

    // TODO LOTS MORE TESTS ON THE VARIOUS FORMS OF GETTERS

    @Test
    public void findingByNonexistentNameReturnsEmptyList() {
        List<Venue> venues = venueDao.findVenues("Qwertyuiop", null, null, new PaginationData(0, 10));
        assertThat(venues.size(), is(0));
    }

    @Test
    public void findVenueByNameReturnsCorrectResults() {
        List<Venue> venues = venueDao.findVenues("PANT", null, null, new PaginationData(0, 10));
        assertThat(venues.size(), is(1));
        assertThat(venues.get(0).getId(), is(1));

        venues = venueDao.findVenues("thE", null, null, new PaginationData(0, 10));
        assertThat(venues.size(), is(2));
        assertThat(venues.get(0).getId(), is(5));
        assertThat(venues.get(1).getId(), is(6));
    }

    @Test
    public void findingVenuesViaPaginationWorks() {
        assertThat(venueDao.findNumberOfVenues(), is(9));
        List<Venue> venues = venueDao.findVenues(null, null, null, new PaginationData(0, 3));
        assertThat(venues.size(), is(3));
        venues = venueDao.findVenues(null, null, null, new PaginationData(1, 3));
        assertThat(venues.size(), is(3));
        venues = venueDao.findVenues(null, null, null, new PaginationData(2, 3));
        assertThat(venues.size(), is(3));
    }

    @Test
    public void findingVenuesByEventId() {
        List<Venue> venues = venueDao.findVenues(null, 8, null, new PaginationData(0, 10));
        assertThat(venues.size(), is(2));
        assertThat(venues.get(0).getId(), is(4));
    }

    @Test
    public void testFindVenuesByCircleSearch() {
        List<Venue> venues = venueDao.findVenues(null, null, new Circle(-34.149885, 62.000001, 100),
                new PaginationData(0, 10));
        assertThat(venues.size(), is(1));
        assertThat(venues.get(0).getId(), is(10));
    }

    @Test
    public void testFindVenuesWithCircleNowhereNearVenues() {
        List<Venue> venues = venueDao.findVenues(null, null, new Circle(37.149885, 60.000001, 100), new PaginationData(
                0, 10));
        assertThat(venues.size(), is(0));
    }

    @Test
    public void testGetProperies() {
        Map<String, String> properties = venueDao.findProperties(2);
        assertThat(properties.size(), is(2));
        assertThat(properties.get("Parking"), is("Valet only"));
        assertThat(properties.get("Ages"), is("18+"));
    }
    
    @Test
    public void testGetMaxKeyValueForVenueComments() {
        int maxValue = venueDao.findMaxKeyValueForComments();
        assertThat(maxValue, is(3));
    }

    @Test
    public void testGetVenueCommentsIsSortedByPostDate() {
        List<Comment> comments = venueDao.findComments(10, new PaginationData(0, 10));
        long timestamp = comments.get(0).getPostDate().getMillis();
        for (int i = 1; i < comments.size(); i++) {
            long nextTimestamp = comments.get(i).getPostDate().getMillis();
            assertTrue(nextTimestamp >= timestamp);
            timestamp = nextTimestamp;
        }
    }

    @Test
    public void testGetVenueComments() {
        List<Comment> comments = venueDao.findComments(10, new PaginationData(0, 10));
        DateTime knownCommentTime = new DateTime(2012, 3, 30, 12, 34, 56);
        assertThat(comments.size(), is(2));
        assertThat(comments.get(0).getId(), is(1));
        assertThat(comments.get(0).getBody(), is("This venue sux."));
        assertThat(comments.get(0).getAuthor().getId(), is(1));
        assertThat(comments.get(0).getAuthor().getEmail(), is("40mpg@gmail.com"));
        assertThat(comments.get(0).getAuthor().getFirstName(), is("Honda"));
        assertThat(comments.get(0).getAuthor().getLastName(), is("Prius"));
        assertThat(comments.get(0).getAuthor().getNickname(), is("hybrid"));
        assertThat(comments.get(0).getPostDate().getMillis(), is(knownCommentTime.getMillis()));
        assertThat(comments.get(1).getId(), is(2));
    }

    @Test
    public void createVenueCommentIncrementsSize() {
        int initialSize = venueDao.findComments(10, new PaginationData(0, 10)).size();
        venueDao.addComment(10, new Comment(null, "Boo", sampleDateTime, sampleUser));
        int afterSize = venueDao.findComments(10, new PaginationData(0, 10)).size();
        assertThat(afterSize, is(initialSize + 1));
    }

    @Test
    public void createdVenueCommentAutoGeneratesId() {
        int maxKeyValue = venueDao.findMaxKeyValueForComments();
        venueDao.addComment(10, new Comment(null, "Boo", sampleDateTime, sampleUser));
        int nextKeyValue = venueDao.findMaxKeyValueForComments();
        assertThat(nextKeyValue, is(maxKeyValue + 1));
    }

    @Test
    public void createdVenueCommentCanBeFound() {
        Comment newComment = new Comment(null, "Ole!", sampleDateTime, sampleUser);
        int lastGeneratedIdValue = venueDao.findMaxKeyValueForComments();
        venueDao.addComment(10, newComment);
        List<Comment> comments = venueDao.findComments(10, new PaginationData(0, 10));
        assertThat(comments.get(1).getId(), is(lastGeneratedIdValue + 1));
        assertThat(comments.get(1).getAuthor(), is(sampleUser));
        assertThat(comments.get(1).getBody(), is("Ole!"));
        assertThat(comments.get(1).getPostDate().getMillis(), is(sampleDateTime.getMillis()));
    }

    @Test
    public void deleteVenueCommentDecrementsSize() {
        int initialSize = venueDao.findComments(10, new PaginationData(0, 10)).size();
        venueDao.deleteComment(10, 2);
        int afterSize = venueDao.findComments(10, new PaginationData(0, 10)).size();
        assertThat(afterSize, is(initialSize - 1));
    }

    @Test
    public void deletedVenueCommentCanNoLongerBeFound() {
        List<Comment> beforeDeletion = venueDao.findComments(6, new PaginationData(0, 10));
        assertThat(beforeDeletion.get(0).getId(), is(3));
        assertThat(beforeDeletion.get(0).getBody(), is("pizza pizza"));
        assertThat(beforeDeletion.size(), is(1));
        venueDao.deleteComment(6, 3);
        List<Comment> comments = venueDao.findComments(6, new PaginationData(0, 10));
        assertThat(comments.size(), is(0));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
