package ua.gabz.dm.database.models;

/**
 * Created by destr on 04.04.2020.
 */

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "USERS")
public class Users {

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

    @Transient
    private String passwordConfirm;

    @Column(name = "last_seen")
    private String lastSeen;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "id")
    private Set<Messages> sendedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Messages> takedMessages;

    public Users() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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


    public Set<Messages> getSendedMessages() {
        return sendedMessages;
    }

    public void setSendedMessages(Set<Messages> sendedMessages) {
        this.sendedMessages = sendedMessages;
    }

    public Set<Messages> getTakedMessages() {
        return takedMessages;
    }

    public void setTakedMessages(Set<Messages> takedMessages) {
        this.takedMessages = takedMessages;
    }
}
