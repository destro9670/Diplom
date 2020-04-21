package db.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
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

    @Column(name = "last_seen", nullable = false)
    private String lastSeen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Message> sendedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Message> takedMessages;

    @ManyToMany(mappedBy = "users")
    private List<Room> rooms;

    public User() {
    }

    public User(String name,
                String surname,
                String nick,
                String login,
                String password,
                String lastSeen) {

        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.login = login;
        this.password = password;
        this.lastSeen = lastSeen;
    }



}

