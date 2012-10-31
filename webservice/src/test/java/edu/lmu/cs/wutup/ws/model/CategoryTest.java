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
        Category c = new Category("Alcohol");
        assertThat(c.getName(), is("Alcohol"));
    }
    
    @Test
    public void testCategoryConstructorWithNameAndParent() {
        Category c = new Category("Alcohol", 15);
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getParentId(), is(15));
    }
    
    @Test
    public void testCategoryConstructorWithIdAndName() {
        Category c = new Category(3, "Alcohol");
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getId(), is(3));
    }
    
    @Test
    public void testCategoryConstructorWithIdNameAndParentId() {
        Category c = new Category(3, "Alcohol", 15);
        assertThat(c.getName(), is("Alcohol"));
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(15));
    }
    
    @Test
    public void checkCategorySetId() {
        Category c = new Category(3, "Alcohol", 15);
        assertThat(c.getId(), is(3));
        c.setId(5);
        assertThat(c.getId(), is(5));
        assertThat(c.getParentId(), is(15));
        assertThat(c.getName(), is("Alcohol"));
    }
    
    @Test
    public void checkCategorySetParentId() {
        Category c = new Category(3, "Alcohol", 15);
        assertThat(c.getParentId(), is(15));
        c.setParentId(17);
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(17));
        assertThat(c.getName(), is("Alcohol"));
    }
    
    @Test
    public void checkCategorySetName() {
        Category c = new Category(3, "Alcohol", 15);
        assertThat(c.getName(), is("Alcohol"));
        c.setName("Kegger");
        assertThat(c.getId(), is(3));
        assertThat(c.getParentId(), is(15));
        assertThat(c.getName(), is("Kegger"));
    }
    
    @Test
    public void equalsUsesIdNameAndParentId() {
        assertThat(new Category(1, "Alcohol", 15), equalTo(new Category(1, "Alcohol", 15)));
        assertThat(new Category(1, "Alcohol", 15), not(equalTo(new Category(1, "Alcohol", 16))));
        assertThat(new Category(1, "Alcohol", 15), not(equalTo(new Category(1, "Not Alcohol", 15))));
        assertThat(new Category(1, "Kegger", 15), not(equalTo(new Category(2, "Kegger", 15))));
        assertFalse(new Category(1, "Kegger").equals(5000));
        assertFalse(new Category(1, "Intergalactic Kegger").equals(null));

    }

    @Test
    public void hashCodeConsidersIdNameAndParentId() {
        Category c = new Category(1, "Kegger", 15);
        Category c2 = new Category(1, "Kegger", 15);
        Category notC = new Category(1, "Alcohol", 15);
        assertThat(c.hashCode(), is(c2.hashCode()));
        assertThat(c.hashCode(), is(not(notC.hashCode())));
    }
    
    @Test
    public void hashCodeConsidersIdAndName() {
        Category c = new Category(1, "Kegger");
        Category c2 = new Category(1, "Kegger");
        Category notC = new Category(1, "Kegger", 15);
        assertThat(c.hashCode(), is(c2.hashCode()));
        assertThat(c.hashCode(), is(not(notC.hashCode())));
    }
}
