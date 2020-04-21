package db.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @SequenceGenerator(name = "room_seq", sequenceName = "room_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "room_seq")
    private long id;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "users_in_rooms",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Message> messages;

    @Column(name = "name")
    private String name;

    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

}
