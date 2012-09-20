package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class UserTest {
    
    @Test
    public void fieldsSetByConstructorCanBeRead() {
        User u = new User(1492, "Joe", "Coffee", "jcoffee@gmail.com","jc1989");
        assertThat(u.getId(), is(1492));
        assertThat(u.getFirstName(), is("Joe"));
        assertThat(u.getLastName(), is("Coffee"));
        assertThat(u.getEmail(), is("jcoffee@gmail.com"));
        assertThat(u.getNickname(), is("jc1989"));
    }
    
    @Test
    public void fieldsSetBySettersCanBeRead() {
        User u = new User();
        u.setId(1214);
        u.setFirstName("John");
        u.setLastName("Silver");
        u.setEmail("admiralty@ymail.com");
        u.setNickname("seapirate");
        assertThat(u.getId(), is(1214));
        assertThat(u.getFirstName(), is("John"));
        assertThat(u.getLastName(), is("Silver"));
        assertThat(u.getEmail(), is("admiralty@ymail.com"));
        assertThat(u.getNickname(), is("seapirate"));
    }
    
    @Test
    public void getFullNameIsCorrect() {
    	User u = new User();
    	u.setFirstName("Renly");
    	u.setLastName("Cheeseburger");
    	assertThat(u.getFullName(), is("Renly Cheeseburger"));
    }
    
    @Test
    public void equalsUsesEmailAndIdOnly() {
    	assertThat(new User(1, "abcdefg@gmail.com"), equalTo(new User(1, "abcdefg@gmail.com")));
    	assertThat(new User(1, "dward@yahoo.com"), not(equalTo(new User(1, "notdward@yahoo.com"))));
    	assertThat(new User(1, "dward@yahoo.com"), not(equalTo(new User(999, "dward@yahoo.com"))));
    	assertFalse(new User(21, "tarik@lion.lmu.edu").equals(5000));
    	assertFalse(new User(1776, "tjefferson@whitehouse.gov").equals(null));
    	
    }
    
    public void hashCodeReturnsId() {
    	User u = new User();
    	u.setId(9999);
    	assertThat(u.hashCode(), is(9999));
    }
}
