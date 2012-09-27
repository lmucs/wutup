package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.Before;
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
    public void creatingIncrementsSize() {
        User u = new User(7, "Dodger", "Duck", "dd@gmail.com", null);
        int beforeSize = userDao.findNumberOfUsers();
        userDao.createUser(u);
        assertThat(userDao.findNumberOfUsers(), is(beforeSize + 1));
    }
}
