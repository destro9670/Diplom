package db.models;

import lombok.Data;

import javax.persistence.*;

@Data
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


}
