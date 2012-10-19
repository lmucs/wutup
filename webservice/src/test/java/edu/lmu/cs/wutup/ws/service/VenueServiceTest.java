package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.VenueDao;
import edu.lmu.cs.wutup.ws.exception.VenueExistsException;
import edu.lmu.cs.wutup.ws.model.Venue;

public class VenueServiceTest {

    VenueServiceImpl service;
    VenueDao dao;

    Venue sampleVenue = new Venue(1, "Sample Venue", "5639 Topanga Canyon Rd Blubberville, Utah 80832");

    @Before
    public void setUp() {
        service = new VenueServiceImpl();
        dao = mock(VenueDao.class);
        service.venueDao = dao;
    }

    @Test
    public void creationDelegatesToDao() {
        service.createVenue(sampleVenue);
        verify(dao).createVenue(sampleVenue);
    }

    @Test(expected=VenueExistsException.class)
    public void creationPropagatesExistExceptions() {
        doThrow(new VenueExistsException()).when(dao).createVenue(sampleVenue);
        service.createVenue(sampleVenue);
    }

    @Test
    public void updatesDelegateToDao() {
        service.updateVenue(sampleVenue);
        verify(dao).updateVenue(sampleVenue);
    }

    @Test
    public void findingDelgatesToDao() {
        when(dao.findVenueById(1)).thenReturn(sampleVenue);
        assertThat(service.findVenueById(1), equalTo(sampleVenue));

    }

    @Test
    public void deletionDelegatesToDao() {
        service.deleteVenue(sampleVenue.getId());
        verify(dao).deleteVenue(sampleVenue.getId());
    }
}
