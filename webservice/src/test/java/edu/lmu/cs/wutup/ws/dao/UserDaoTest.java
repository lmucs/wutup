package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.model.User;

public class UserDaoTest {
    
    private EmbeddedDatabase database;
    private UserDaoJdbcImpl userDao = new UserDaoJdbcImpl();
    
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
        // assertThat(userDao.findNumberOfUsers(), is(6));
    }
    
    @Test
    public void creatingIncrementsSize() {
        User u = new User(9, "Dodger", "Duck", "dd@gmail.com", null);
        int beforeSize = userDao.findNumberOfUsers();
        userDao.createUser(u);
        assertThat(userDao.findNumberOfUsers(), is(beforeSize + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        User u = new User(9, "nobody@gmail.com");
        userDao.createUser(u);
        int initialSize = userDao.findNumberOfUsers();
        userDao.deleteUser(u);      
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
    @Test(expected=NoSuchUserException.class)
    public void deletingNonExistantUserThrowsException() {
        User u = new User(9999, "notreal@gmail.com");
        userDao.deleteUser(u);
    }

    @Test(expected=NoSuchUserException.class)
    public void updatingNonExistantUserThrowsException() {
        User u = new User(9998, "notreal@gmail.com");
        userDao.updateUser(u);
    }
    
    @Test(expected=NoSuchUserException.class)
    public void findingNonExistantUs9999999erThrowsException() {
        userDao.findUserById(2012);
    }

    @Test //TODO need to automate user ids
    public void updatedUserColumnsCanBeRead() {
        userDao.createUser(new User(9, "Busby", "Fernjoy", "bf@lol.com", "bferny"));
        User u = userDao.findUserById(9);
        u.setEmail("fernjoy@aol.com");
        u.setNickname("greenhouser");
        userDao.updateUser(u);
        
        User newer = userDao.findUserById(9);
        assertThat(newer.getEmail(), is(u.getEmail()));
        assertThat(newer.getNickname(), is(u.getNickname()));//TODO need to automate user ids
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
    
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
