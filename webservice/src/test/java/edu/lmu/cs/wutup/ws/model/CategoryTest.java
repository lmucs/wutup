package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CategoryTest {

    @Test
    public void testCategoryConstructorWithName() {
        Category c = Category.builder().name("Alcohol").build();
        assertThat(c.getName(), is("Alcohol"));
    }

    @Test
    public void testCategoryConstructorWithNameAndParent() {
        Category c = Category.builder().name("Alcohol").parentId(15).build();
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getParentId(), is(15));
    }

    @Test
    public void testCategoryConstructorWithIdAndName() {
        Category c = Category.builder().id(3).name("Alcohol").build();
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getId(), is(3));
    }

    @Test
    public void testCategoryConstructorWithIdNameAndParentId() {
        Category c = Category.builder().id(3).name("Alcohol").parentId(15).build();
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(15));
    }

    @Test
    public void checkCategorySetId() {
        Category c = Category.builder().id(3).name("Alcohol").parentId(15).build();
        assertThat(c.getId(), is(3));
        c.setId(5);
        assertThat(c.getId(), is(5));
        assertThat(c.getParentId(), is(15));
        assertThat(c.getName(), is("Alcohol"));
    }

    @Test
    public void checkCategorySetParentId() {
        Category c = Category.builder().id(3).name("Alcohol").parentId(15).build();
        assertThat(c.getParentId(), is(15));
        c.setParentId(17);
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(17));
        assertThat(c.getName(), is("Alcohol"));
    }

    @Test
    public void checkCategorySetName() {
        Category c = Category.builder().id(3).name("Alcohol").parentId(15).build();
        assertThat(c.getName(), is("Alcohol"));
        c.setName("Kegger");
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(15));
        assertThat(c.getName(), is("Kegger"));
    }

    @Test
    public void equalsUsesIdNameAndParentId() {
        assertThat(Category.builder().id(1).name("Alcohol").parentId(15).build(),
                equalTo(Category.builder().id(1).name("Alcohol").parentId(15).build()));
        assertThat(Category.builder().id(1).name("Alcohol").parentId(15).build(),
                not(equalTo(Category.builder().id(1).name("Alcohol").parentId(16).build())));
        assertThat(Category.builder().id(1).name("Alcohol").parentId(15).build(),
                not(equalTo(Category.builder().id(1).name("Not Alcohol").parentId(15).build())));
        assertThat(Category.builder().id(1).name("Kegger").parentId(15).build(),
                not(equalTo(Category.builder().id(2).name("Kegger").parentId(15).build())));
        assertFalse(Category.builder().id(1).name("Kegger").build().equals(5000));
        assertFalse(Category.builder().id(1).name("Integalactic Kegger").build().equals(null));

    }

    @Test
    public void hashCodeConsidersIdNameAndParentId() {
        Category c = Category.builder().id(1).name("Kegger").parentId(15).build();
        Category c2 = Category.builder().id(1).name("Kegger").parentId(15).build();
        Category notC = Category.builder().id(1).name("Alcohol").parentId(15).build();
        assertThat(c.hashCode(), is(c2.hashCode()));
        assertThat(c.hashCode(), is(not(notC.hashCode())));
    }

    @Test
    public void hashCodeConsidersIdAndName() {
        Category c = Category.builder().id(1).name("Kegger").build();
        Category c2 = Category.builder().id(1).name("Kegger").build();
        Category notC = Category.builder().id(1).name("Kegger").parentId(15).build();
        assertThat(c.hashCode(), is(c2.hashCode()));
        assertThat(c.hashCode(), is(not(notC.hashCode())));
    }
}
