package edu.lmu.cs.wutup.ws.resource;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.resource.UserResource;
import edu.lmu.cs.wutup.ws.service.UserService;
import edu.lmu.cs.wutup.ws.model.User;

public class UserResourceTest {

    UserResource resource;
    UserService service;
    UriInfo sampleUriInfo;
    User sampleUser = new User(1, "heyheyhey@gmail.com");
    
    @Before
    public void setUp() {
        resource = new UserResource();
        service = mock(UserService.class);
        resource.userService = service;
        sampleUriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    }
    
    @Test
    public void createUserRespondsWithNewLocation() {
        Response response = resource.createUser(sampleUser, sampleUriInfo);
        verify(service).createUser(sampleUser);
        assertThat(response.getStatus(), is(201));
    }


}
