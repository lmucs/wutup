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

import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.Venue;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database. The database is setup and torn down
 * around each test so that the tests don't affect each other.
 */
public class VenueDaoTest {

    private EmbeddedDatabase database;
    private VenueDaoJdbcImpl venueDao = new VenueDaoJdbcImpl();

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

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
