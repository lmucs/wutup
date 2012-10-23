package edu.lmu.cs.wutup.ws.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.lmu.cs.wutup.ws.exception.CategoryExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCategoryException;
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
        Category c = new Category(4, "Park");
        int beforeSize = categoryDao.findNumberOfCategories();
        categoryDao.createCategory(c);
        assertThat(categoryDao.findNumberOfCategories(), is(beforeSize + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        Category c = new Category(4, "Park");
        categoryDao.createCategory(c);
        int initialSize = categoryDao.findNumberOfCategories();
        categoryDao.deleteCategory(c.getId());      
        assertThat(categoryDao.findNumberOfCategories(), is(initialSize - 1));
    }
    
    @Test
    public void createdCategoryCanBeFound() {
        Category c = new Category(4, "Park");
        categoryDao.createCategory(c);
        Category result = categoryDao.findCategoryById(4);
        assertThat(result.getId(), is(c.getId()));
        assertThat(result.getName(), is(c.getName()));
    }
    
    
    @Test(expected=CategoryExistsException.class)
    public void creatingCategoryWithDuplicateIdThrowsException() {
        Category c = new Category(1, "Theater");
        categoryDao.createCategory(c);
    }
    
    
    @Test
    public void createCategoryWithNullIdAndNullParentGeneratesId() {
        Category c = new Category("Alcohol");
        categoryDao.createCategory(c);
        assertThat(c.getId(), is(4));
    }
    
    @Test
    public void createCategoryWithNullIdGeneratesId() {
        Category c = new Category("Musical", 1);
        categoryDao.createCategory(c);
        assertThat(c.getId(), is(4));
    }
    
    
    @Test(expected=NoSuchCategoryException.class)
    public void deletingNonExistentCategoryThrowsException() {
        Category c = new Category(57, "Party");
        categoryDao.deleteCategory(c.getId());
    }

    @Test(expected=NoSuchCategoryException.class)
    public void updatingNonExistentCategoryThrowsException() {
        Category c = new Category(57, "Party");
        categoryDao.updateCategory(c);
    }
    
    
    @Test(expected=NoSuchCategoryException.class)
    public void findingNonExistantCategoryThrowsException() {
        categoryDao.findCategoryById(2012);
    }
    /*
    @Test
    public void updatedCategoryColumnsCanBeRead() {
        categoryDao.createCategory(new Category(9, "Club meeting"));
        Category c = categoryDao.findCategoryById(9);
        c.setName("Fun meeting");
        categoryDao.updateCategory(c);
        
        Category newer = categoryDao.findCategoryById(9);
        assertThat(newer.getName(), is(c.getName()));
    }*/
    
    @Test
    public void getMaxIdValueReturnsCorrectValue() {
        categoryDao.createCategory(new Category(9999, "Kick back"));
        int max = categoryDao.getMaxValueFromColumn("id");
        assertEquals(max, 9999);
    }
    
    @Test
    public void getNextUsableUserIdReturnsMaxPlusOne() {
        int currentMaxIdValue = categoryDao.getMaxValueFromColumn("id");
        int nextGeneratedUserId = categoryDao.getNextUsableCategoryId();
        assertEquals(nextGeneratedUserId, currentMaxIdValue + 1);
    }
    
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
