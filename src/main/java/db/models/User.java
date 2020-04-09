package db.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "surname")
    @NotNull
    private String surname;

    @Column(name = "nick")
    @NotNull
    private String nick;

    @Column(name = "login")
    @NotNull
    private String login;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "key_date")
    @NotNull
    private String keyDate;

    @Column(name = "active")
    @NotNull
    private boolean active;

    @Column(name = "last_seen")
    @NotNull
    private String lastSeen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Message> sendedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Message> takedMessages;

    public User() {
    }

    public User(@NotNull String name,
                @NotNull String surname,
                @NotNull String nick,
                @NotNull String login,
                @NotNull String password,
                @NotNull String keyDate,
                @NotNull boolean active,
                @NotNull String lastSeen) {
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.login = login;
        this.password = password;
        this.keyDate = keyDate;
        this.active = active;
        this.lastSeen = lastSeen;
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

