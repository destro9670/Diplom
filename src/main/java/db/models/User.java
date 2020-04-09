package db.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "nick")
    private String nick;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "key_date")
    private String keyDate;

    @Column(name = "active")
    private boolean active;

    @Column(name = "last_seen")
    private String lastSeen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Message> sendedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Message> takedMessages;

    ///TODO(1) create constructor for add new users into database
    public User() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(String keyDate) {
        this.keyDate = keyDate;
    }


    public Set<Message> getSendedMessages() {
        return sendedMessages;
    }

    public void setSendedMessages(Set<Message> sendedMessages) {
        this.sendedMessages = sendedMessages;
    }

    public Set<Message> getTakedMessages() {
        return takedMessages;
    }

    public void setTakedMessages(Set<Message> takedMessages) {
        this.takedMessages = takedMessages;
    }
}

