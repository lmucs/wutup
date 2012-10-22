package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.model.Category;

public class CategoryDaoTest {
    private EmbeddedDatabase database;
    private CategoryDaoJdbcImpl categoryDao = new CategoryDaoJdbcImpl();
    
    @Before
    public void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("init.sql")
                .build();
        categoryDao.jdbcTemplate = new JdbcTemplate(database);
    }
    
    @Test
    public void tableHasCorrectSizeFromSetup() {
        assertThat(categoryDao.findNumberOfCategories(), is(3));
    }
    
    @Test
    public void creatingIncrementsSize() {
        Category c = new Category(4, null, "Park");
        int beforeSize = categoryDao.findNumberOfCategories();
        categoryDao.createCategory(c);
        assertThat(categoryDao.findNumberOfCategories(), is(beforeSize + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        Category c = new Category(4, null, "Park");
        categoryDao.createCategory(c);
        int initialSize = categoryDao.findNumberOfCategories();
        categoryDao.deleteCategory(c.getId());      
        assertThat(categoryDao.findNumberOfCategories(), is(initialSize - 1));
    }
    
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
