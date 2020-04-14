package db.dao;

import db.models.Room;
import db.models.User;
import db.models.UserInRoom;

import java.util.List;

public interface UserInRoomDAO {

    List<Room> findRoomByUser(User user);

    List<User> findUserByRoom(Room room);

    void save(UserInRoom userInRoom);

    void delete(Room room);
}
