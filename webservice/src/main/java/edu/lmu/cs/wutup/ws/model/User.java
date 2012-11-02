package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "user")
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String nickname;

    public User() {
        // No-arg constructor required for annotations
    }

    public User(Integer id, String email) {
        this(id, null, null, email, null);
    }

    public User(String first, String last, String email, String nick) {
        this(null, first, last, email, nick);
    }

    public User(Integer id, String first, String last, String email, String nick) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.nickname = nick;
    }

    @XmlElement(name = "id")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer i) {
        this.id = i;
    }

    @XmlElement(name = "firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement(name = "lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, email);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof User) {
            User u = User.class.cast(obj);
            result = Objects.equal(id, u.id) && Objects.equal(u.email, this.email);
        }

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("name", this.getFullName())
                .add("email", this.email)
                .add("nickname", this.nickname)
                .toString();
    }
}
