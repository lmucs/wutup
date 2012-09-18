package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class UserTest {
    
    @Test
    public void fieldsSetByConstructorCanBeRead() {
        User u = new User("Joe", "Coffee", "jcoffee@gmail.com","jc1989");
        assertThat(u.getFirstName(), is("Joe"));
        assertThat(u.getLastName(), is("Coffee"));
        assertThat(u.getEmail(), is("jcoffee@gmail.com"));
        assertThat(u.getNickname(), is("jc1989"));
    }
    
    @Test
    public void fieldsSetBySettersCanBeRead() {
        User u = new User();
        u.setFirstName("John");
        u.setLastName("Silver");
        u.setEmail("admiralty@ymail.com");
        u.setNickname("seapirate");
        assertThat(u.getFirstName(), is("John"));
        assertThat(u.getLastName(), is("Silver"));
        assertThat(u.getEmail(), is("admiralty@ymail.com"));
        assertThat(u.getNickname(), is("seapirate"));
    }
}
