package db.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User  {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "user_seq")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "nick", nullable = false)
    private String nick;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "key_date", nullable = false)
    private String keyDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "last_seen", nullable = false)
    private String lastSeen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Message> sendedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Message> takedMessages;

    @ManyToMany(mappedBy ="users")
    private List<Room> rooms;

    public User() {
    }

    public User(String name,
                String surname,
                String nick,
                String login,
                String password,
                String keyDate,
                boolean active,
                String lastSeen) {

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


    public List<Message> getSendedMessages() {
        return sendedMessages;
    }

    public void setSendedMessages(List<Message> sendedMessages) {
        this.sendedMessages = sendedMessages;
    }

    public List<Message> getTakedMessages() {
        return takedMessages;
    }

    public void setTakedMessages(List<Message> takedMessages) {
        this.takedMessages = takedMessages;
    }


    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nick='" + nick + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", keyDate='" + keyDate + '\'' +
                ", active=" + active +
                ", lastSeen='" + lastSeen + '\'' +
                '}';
    }
}

