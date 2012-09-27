package edu.lmu.cs.wutup.ws.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class UserDaoTest {
    
    private EmbeddedDatabase database;
    private UserDaoJdbcImpl userDao = new UserDaoJdbcImpl();

}
