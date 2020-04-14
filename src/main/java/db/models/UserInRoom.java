package db.models;

import javax.persistence.*;

@Entity
@Table(name = "users_in_rooms")
public class UserInRoom {

    @Id
    @SequenceGenerator(name = "user_in_room_seq", sequenceName = "users_in_rooms_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "user_in_room_seq")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @ManyToOne(targetEntity = Room.class)
    @JoinColumn(name = "room_id" , nullable = false)
    private Room room;

    public UserInRoom(User user, Room room) {
        this.user = user;
        this.room = room;
    }

    public UserInRoom() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
