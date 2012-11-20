package edu.lmu.cs.wutup.ws.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.VenueService;

public class VenueResourceTest {
    VenueResource resource;
    VenueService service;

    Venue sampleVenue = new Venue(1, "Alice", null);
    Comment sampleVenueComment = new Comment(1, "body", new DateTime(), new User());
    List<Venue> sampleVenueList = new ArrayList<Venue>();
    List<Comment> sampleVenueCommentList = new ArrayList<Comment>();
    String samplePropertyKey = "Sample key";
    String samplePropertyValue = "Sample value";
    Map<String, String> sampleProperties;
    Map<String, String> updateProperties;
    UriInfo sampleUriInfo;

    @Before
    public void setUp() {
        resource = new VenueResource();
        service = mock(VenueService.class);
        resource.venueService = service;
        sampleUriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        sampleProperties = new HashMap<String, String>();
        updateProperties = new HashMap<String, String>();
        sampleProperties.put(samplePropertyKey, samplePropertyValue);
    }

    @Test
    public void getVenuePropertiesResponds200() {
        when(service.findProperties(2)).thenReturn(sampleProperties);
        assertThat(resource.getProperties("2"), equalTo(sampleProperties));
    }

    @Test
    public void getVenuePropertiesWithNonIntegerProduces400() {
        try {
            resource.getProperties("abcd");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void getVenuePropertiesWithNoIdProduces400() {
        try {
            resource.getProperties("");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void updatePropertyWithNoIdProduces400() {
        try {
            resource.updateProperty("", sampleProperties);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void updatePropertyWithNonIntegerIdProduces400() {
        try {
            resource.updateProperty("abcd", sampleProperties);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    /* Begin Venue Comment Testing */
    @Test
    public void canAddCommentsToVenue() {
        resource.addComment("1", sampleVenueComment);
        verify(service).addComment(1, sampleVenueComment);
    }

    @Test
    public void updatingCommentProducesHttp204() {
        Response response = resource.updateComment("1", "1", sampleVenueComment);
        verify(service).updateComment(1, sampleVenueComment);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingCommentProducesHttp204() {
        Response response = resource.deleteComment("1", "1");
        verify(service).deleteComment(1, 1);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void creatingVenueNullsOutIdAndResponds201WithLocation() {
        Response response = resource.createVenue(sampleVenue, sampleUriInfo);
        verify(service).createVenue(sampleVenue);
        assertThat(response.getStatus(), is(201));
        assertThat(response.getMetadata().getFirst("Location").toString(),
                is("http://example.com/" + sampleVenue.getId()));
    }

    @Test
    public void findingCommentsByVenueIdReturnsList() {
        when(service.findComments(1, new PaginationData(1, 10))).thenReturn(sampleVenueCommentList);
        List<Comment> result = resource.findVenueComments("1", "1", "10");
        assertThat(result, is(sampleVenueCommentList));
    }

    @Test
    public void findingCommentsWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findVenueComments("1", "1", "51");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingCommentsWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findVenueComments("1", "0", "0");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }
}
