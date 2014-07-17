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
        Category c = Category.builder().id(4).name("Park").build();
        int beforeSize = categoryDao.findNumberOfCategories();
        categoryDao.createCategory(c);
        assertThat(categoryDao.findNumberOfCategories(), is(beforeSize + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        Category c = Category.builder().id(4).name("Park").build();
        categoryDao.createCategory(c);
        int initialSize = categoryDao.findNumberOfCategories();
        categoryDao.deleteCategory(c.getId());
        assertThat(categoryDao.findNumberOfCategories(), is(initialSize - 1));
    }

    @Test
    public void createdCategoryCanBeFound() {
        Category c = Category.builder().id(4).name("Park").build();
        categoryDao.createCategory(c);
        Category result = categoryDao.findCategoryById(4);
        assertThat(result.getId(), is(c.getId()));
        assertThat(result.getName(), is(c.getName()));
    }

    @Test(expected=CategoryExistsException.class)
    public void creatingCategoryWithDuplicateIdThrowsException() {
        Category c = Category.builder().id(1).name("Theater").build();
        categoryDao.createCategory(c);
    }

    @Test
    public void createCategoryWithNullIdAndNullParentGeneratesId() {
        Category c = Category.builder().name("Alcohol").build();
        categoryDao.createCategory(c);
        assertThat(c.getId(), is(4));
    }

    @Test
    public void createCategoryWithNullIdGeneratesId() {
        Category c = Category.builder().parentId(1).name("Musical").build();
        categoryDao.createCategory(c);
        assertThat(c.getId(), is(4));
    }


    @Test(expected=NoSuchCategoryException.class)
    public void deletingNonExistentCategoryThrowsException() {
        Category c = Category.builder().id(57).name("Party").build();
        categoryDao.deleteCategory(c.getId());
    }

    @Test(expected=NoSuchCategoryException.class)
    public void updatingNonExistentCategoryThrowsException() {
        Category c = Category.builder().id(57).name("Party").build();
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
        categoryDao.createCategory(Category.builder().id(9999).name("Kick back").build());
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
