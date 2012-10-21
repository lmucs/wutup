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
    public void createdVenueCanBeFound() {
        venueDao.createVenue(new Venue(9, "A venue name", "this is an address"));
        Venue e = venueDao.findVenueById(9);
        assertThat(e.getId(), is(9));
        assertThat(e.getName(), is("A venue name"));
    }

    @Test
    public void updatesToCreatedVenueCanBeRead() {
        venueDao.createVenue(new Venue(9, "A venue name", "this is an address"));
        Venue v = venueDao.findVenueById(9);
        v.setName("Cricket Game");
        v.setAddress("this is a REAL address");
        venueDao.updateVenue(v);
        v = venueDao.findVenueById(9);
        assertThat(v.getId(), is(9));
        assertThat(v.getName(), is("Cricket Game"));
        assertThat(v.getAddress(), is("this is a REAL address"));
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
        assertThat(venueDao.findNumberOfVenues(), is(8));
    }

    @Test
    public void venueAliceIsInInitialDataSet() {
        List<Venue> venues = venueDao.findVenuesByAddress("6233 Hollywood Bl, Los Angeles, CA", 0, 12);
        assertThat(venues.size(), is(1));

    }

    @Test
    public void findingByNonexistentNameReturnsEmptyList() {
        List<Venue> venues = venueDao.findVenuesByAddress("Qwertyuiop", 0, 10);
        assertThat(venues.size(), is(0));
    }

    @Test
    public void findingVenuesViaPaginationWorks() {
        assertThat(venueDao.findNumberOfVenues(), is(8));
        List<Venue> venues = venueDao.findAllVenues(0, 3);
        assertThat(venues.size(), is(3));
        venues = venueDao.findAllVenues(1, 3);
        assertThat(venues.size(), is(3));
        venues = venueDao.findAllVenues(2, 3);
        assertThat(venues.size(), is(2));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
