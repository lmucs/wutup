package edu.lmu.cs.wutup.ws.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.UserService;

public class UserResourceTest {

    UserResource resource;
    UserService service;
    UriInfo sampleUriInfo;
    User sampleUser = new User(1, "john", "crown",  "heyheyhey@gmail.com", "lolol", "haha");
    PaginationData samplePagination = new PaginationData(0, 10);

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
        assertThat(response.getMetadata().getFirst("Location").toString(),
                is("http://example.com/" + sampleUser.getId()));
    }

    @Test
    public void createDuplicateUserRespondsWith409() {
        try {
            doThrow(new UserExistsException()).when(service).createUser(sampleUser);
            resource.createUser(sampleUser, sampleUriInfo);
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updateUserReturns204() {
        Response response = resource.updateUser("1", sampleUser);
        verify(service).updateUser(sampleUser);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updateUserWithMismatchedIdRespondsWith409() {
        try {
            resource.updateUser("22", sampleUser);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updateNonExistantUserRespondsWith404() {
        try {
            doThrow(new NoSuchUserException()).when(service).updateUser(sampleUser);
            resource.updateUser("1", sampleUser);
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void updateUserDoesNotRequireIdInBody() {
        Response response = resource.updateUser("1", sampleUser);
        verify(service).updateUser(sampleUser);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deleteUserRespondsWith204() {
        Response response = resource.deleteUser("1");
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deleteNonExistantUserResponds404() {
        try {
            doThrow(new NoSuchUserException()).when(service).deleteUser(sampleUser.getId());
            resource.deleteUser("1");
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void getUserByIdReturnsWithRequestedUser() {
        when(service.findUserById(sampleUser.getId())).thenReturn(sampleUser);
        User u = resource.findUserById("1");
        verify(service).findUserById(sampleUser.getId());
        assertThat(u.getId(), is(1));
        assertThat(u.getEmail(), is("heyheyhey@gmail.com"));
    }

    @Test
    public void getNonExistantUserByIdResponds404() {
        try {
            doThrow(new NoSuchUserException()).when(service).findUserById(sampleUser.getId());
            resource.findUserById("1");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void getUserByIdWithNoIdParameterResponds400() {
        try {
            resource.findUserById(null);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }
}
