package db.dao;

import db.models.Room;
import db.models.User;

import java.util.List;

public interface RoomDAO {


    Room findUserById(long id);

    List<Room> findRoomByUser(User user);

    void save(Room room);

    void update(Room room);

    void delete(Room room);

    List<Room> findRoomByName(String name);

    void insertUsersInRoom(Room room, User user1, User user2);
}
