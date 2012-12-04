package edu.lmu.cs.wutup.ws.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 4537099912543978942L;

    private Integer id;
    private String facebookId;
    private String firstName;
    private String lastName;
    private String email;
    private String nickname;
    private String sessionId;

    public User() {
        // No-arg constructor required for annotations
    }

    public User(Integer id, String email) {
        this(id, null, null, email, null, null);
    }

    public User(Integer id, String email, String sessionId, String facebookId) {
        this(id, null, null, email, null, sessionId, facebookId);
    }

    public User(String first, String last, String email, String nick) {
        this(null, first, last, email, nick, null, null);
    }

    public User(String first, String last, String email, String nick, String sessionId, String facebookId) {
        this(null, first, last, email, nick, sessionId, facebookId);
    }

    public User(Integer id, String first, String last, String email, String nick, String facebookId) {
        this(id, first, last, email, nick, null, facebookId);
    }

    public User(Integer id, String first, String last, String email, String nick, String sessionId, String facebookId) {
        this.id = id;
        this.facebookId = facebookId;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.nickname = nick;
        this.sessionId = sessionId;
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

    @XmlElement(name = "sessionId")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlElement(name = "facebookId")
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
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
                .add("email", this.email)
                .add("firstname", this.firstName)
                .add("lastname", this.lastName)
                .add("nickname", this.nickname)
                .toString();
    }
}
