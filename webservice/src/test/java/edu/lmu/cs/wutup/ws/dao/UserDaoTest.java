package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public class UserDaoTest {

    private EmbeddedDatabase database;
    private UserDaoJdbcImpl userDao = new UserDaoJdbcImpl();
    User sampleUser = new User(1, "Honda", "Prius", "40mpg@gmail", "hybrid", "");

    @Before
    public void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        userDao.jdbcTemplate = new JdbcTemplate(database);
    }

    @Test
    public void tableHasCorrectSizeFromSetup() {
        assertThat(userDao.findNumberOfUsers(), is(11));
    }

    @Test
    public void creatingIncrementsSize() {
        User u = new User(9, "Dodger", "Duck", "dd@gmail.com", null, null);
        int beforeSize = userDao.findNumberOfUsers();
        userDao.createUser(u);
        assertThat(userDao.findNumberOfUsers(), is(beforeSize + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        User u = new User(9, "nobody@gmail.com");
        userDao.createUser(u);
        int initialSize = userDao.findNumberOfUsers();
        userDao.deleteUser(u.getId());
        assertThat(userDao.findNumberOfUsers(), is(initialSize - 1));
    }

    @Test
    public void createdUserCanBeFound() {
        User u = new User(9, "me@ymail.com");
        userDao.createUser(u);
        User result = userDao.findUserById(9);
        assertThat(result.getId(), is(u.getId()));
        assertThat(result.getEmail(), is(u.getEmail()));
    }


    @Test(expected=UserExistsException.class)
    public void creatingUserWithDuplicateIdThrowsException() {
        User u = new User(2, "ImABigPhony@hotmail.com");
        userDao.createUser(u);
    }

    @Test
    public void createUserWithNullIdGeneratesId() {
        User u = new User(null, "Fabio");
        userDao.createUser(u);
        assertThat(u.getId(), is(3505));
    }

    @Test(expected=NoSuchUserException.class)
    public void deletingNonExistantUserThrowsException() {
        User u = new User(9999, "notreal@gmail.com");
        userDao.deleteUser(u.getId());
    }

    @Test(expected=NoSuchUserException.class)
    public void updatingNonExistantUserThrowsException() {
        User u = new User(9998, "notreal@gmail.com");
        userDao.updateUser(u);
    }

    @Test(expected=NoSuchUserException.class)
    public void findingNonExistantUserThrowsException() {
        userDao.findUserById(2012);
    }

    @Test //TODO need to automate user ids
    public void updatedUserColumnsCanBeRead() {
        userDao.createUser(new User(9, "Busby", "Fernjoy", "bf@lol.com", "bferny", null));
        User u = userDao.findUserById(9);
        u.setEmail("fernjoy@aol.com");
        u.setNickname("greenhouser");
        userDao.updateUser(u);

        User newer = userDao.findUserById(9);
        assertThat(newer.getEmail(), is(u.getEmail()));
        assertThat(newer.getNickname(), is(u.getNickname()));
    }

    @Test
    public void getMaxIdValueReturnsCorrectValue() {
        userDao.createUser(new User(9999, "abcde@gmail.com"));
        int max = userDao.getMaxValueFromColumn("id");
        assertEquals(max, 9999);
    }

    @Test
    public void getNextUsableUserIdReturnsMaxPlusOne() {
        int currentMaxIdValue = userDao.getMaxValueFromColumn("id");
        int nextGeneratedUserId = userDao.getNextUsableUserId();
        assertEquals(nextGeneratedUserId, currentMaxIdValue + 1);
    }

    @Test
    public void checkRetrievalBySessionId() {
        User u = new User(15, "stuff", "verhasselt", "hah@poof.com", "gratz", "someSessionId12", "somefbid");
        userDao.createUser(u);
        User retrieve = userDao.findUserBySessionId("someSessionId12");
        assertEquals(retrieve.getId(), u.getId());
    }

    @Test
    public void testFindCommentsByUserId() {
        DateTime postDate1 = new DateTime(2012, 3, 17, 0, 0, 0);
        DateTime postDate2 = new DateTime(2012, 3, 30, 12, 34, 56);
        DateTime postDate3 = new DateTime(2012, 12, 25, 7, 0, 0);
        List<Comment> comments = userDao.findCommentsByUser(sampleUser, new PaginationData(0, 10));
        assertThat(comments.size(), is(3));
        assertThat(comments.get(0).getBody(), is("pizza pizza"));
        assertThat(comments.get(0).getAuthor().getNickname(), is("hybrid"));
        assertThat(comments.get(0).getAuthor().getId(), is(1));
        assertThat(comments.get(0).getPostDate().getMillis(), is(postDate3.getMillis()));
        assertThat(comments.get(1).getBody(), is("This venue sux."));
        assertThat(comments.get(1).getAuthor().getNickname(), is("hybrid"));
        assertThat(comments.get(1).getAuthor().getId(), is(1));
        assertThat(comments.get(1).getPostDate().getMillis(), is(postDate2.getMillis()));
        assertThat(comments.get(2).getBody(), is("Boo, sux"));
        assertThat(comments.get(2).getPostDate().getMillis(), is(postDate1.getMillis()));
        assertThat(comments.get(2).getAuthor().getNickname(), is("hybrid"));
        assertThat(comments.get(2).getAuthor().getId(), is(1));
    }
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
