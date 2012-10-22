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

import edu.lmu.cs.wutup.ws.exception.CategoryExistsException;
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
    
    
    /*@Test
    public void createCategoryWithNullIdGeneratesId() {
        Category c = new Category(null, "Alcohol");
        categoryDao.createCategory(c);
        assertThat(c.getId(), is(15));
    }*/
    
    /*
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
    }*/
    
    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
