package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.VenueDao;
import edu.lmu.cs.wutup.ws.exception.VenueExistsException;
import edu.lmu.cs.wutup.ws.model.Venue;

public class VenueServiceTest {

    VenueServiceImpl service;
    VenueDao dao;

    Venue sampleVenue = new Venue(1, "Sample Venue", "5639 Topanga Canyon Rd Blubberville, Utah 80832");
    String samplePropertyKey = "Sample Property Name";
    String samplePropertyValue = "Sample Property Value";
    Map<String, String> samplePropertyMap;

    @Before
    public void setUp() {
        service = new VenueServiceImpl();
        dao = mock(VenueDao.class);
        service.venueDao = dao;
        samplePropertyMap = new HashMap<String, String>();
        samplePropertyMap.put(samplePropertyKey, samplePropertyValue);
    }

    @Test
    public void creationDelegatesToDao() {
        service.createVenue(sampleVenue);
        verify(dao).createVenue(sampleVenue);
    }

    @Test(expected = VenueExistsException.class)
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

    @Test
    public void addPropertyDelegatesToDao() {
        service.addProperty(sampleVenue.getId(), samplePropertyKey, samplePropertyValue);
        verify(dao).addProperty(sampleVenue.getId(), samplePropertyKey, samplePropertyValue);
    }

    @Test
    public void getPropertiesDelegatesToDao() {
        when(dao.findProperties(sampleVenue.getId())).thenReturn(samplePropertyMap);
        assertThat(service.findProperties(sampleVenue.getId()), equalTo(samplePropertyMap));
    }

    @Test
    public void updatePropertyValueDelegatesToDao() {
        service.updateOrAddProperty(sampleVenue.getId(), samplePropertyMap);
        verify(dao).updateOrAddProperty(sampleVenue.getId(), samplePropertyKey, samplePropertyValue);
    }

    @Test
    public void deletePropertyDelegatesToDao() {
        service.deleteProperty(sampleVenue.getId(), samplePropertyKey);
        verify(dao).deleteProperty(sampleVenue.getId(), samplePropertyKey);
    }
}
