package edu.lmu.cs.wutup.ws.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.CommentService;

public class CommentResourceTest {
    CommentResource resource;
    CommentService service;

    User sampleUser = new User( 1000, "first", "last", "email", "nick");
    Comment sampleComment = new Comment("This is a comment.", sampleUser, new DateTime());
    List<Comment> sampleCommentList = new ArrayList<Comment>();
    UriInfo sampleUriInfo;

    @Before
    public void setUp() {
        resource = new CommentResource();
        service = mock(CommentService.class);
        resource.CommentService = service;
        sampleUriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    }

    @Test
    public void creatingDuplicateCommentThrowsException() {
        try {
            doThrow(new CommentExistsException()).when(service).createComment(sampleComment);
            resource.createComment(sampleComment, sampleUriInfo);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingCommentProducesHttp204() {
        Response response = resource.updateComment("1", sampleComment);
        verify(service).updateComment(sampleComment);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updatingCommentWithMismatchedIdThrowsException() {
        try {
            resource.updateComment("7", sampleComment);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingNonexistingCommentThrowsException() {
        try {
            doThrow(new NoSuchCommentException()).when(service).updateComment(sampleComment);
            resource.updateComment("1", sampleComment);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void updatingCommentWithNonIntegerIdThrowsException() {
        try {
            resource.updateComment("zzzzz", sampleComment);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void deletingCommentProducesHttp204() {
        when(service.findCommentById(1)).thenReturn(sampleComment);
        Response response = resource.deleteComment("1");
        verify(service).deleteComment(sampleComment);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingNonexistingCommentThrowsException() {
        try {
            when(service.findCommentById(1)).thenReturn(sampleComment);
            doThrow(new NoSuchCommentException()).when(service).deleteComment(sampleComment);
            resource.deleteComment("1");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void deletingCommentWithNonIntegerIdThrowsException() {
        try {
            resource.deleteComment("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingExistingCommentByIdProducesHttp200() {
        when(service.findCommentById(1)).thenReturn(sampleComment);
        resource.findCommentById("1");
        verify(service).findCommentById(1);
    }

    @Test
    public void findingByIdWithNoIdThrowsException() {
        try {
            resource.findCommentById(null);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingByIdWithBadIdThrowsException() {
        try {
            resource.findCommentById("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingByUserIdReturnsList() {
        when(service.findCommentsByUserId(sampleUser.getId())).thenReturn(sampleCommentList);
        List<Comment> result = resource.findCommentsByUserId("1000");
        assertThat(result, is(sampleCommentList));
    }

}