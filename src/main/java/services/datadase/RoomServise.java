package services.datadase;

import db.dao.RoomDAO;
import db.dao.RoomDAOImpl;
import db.dao.UserInRoomDAO;
import db.dao.UserInRoomDAOImp;
import db.models.Room;
import db.models.User;
import db.models.UserInRoom;

import java.util.List;

public class RoomServise {

    private final RoomDAO roomDAO;

    public RoomServise() {
        this.roomDAO = new RoomDAOImpl();
    }


    public Room findUserById(long id) {
        return roomDAO.findUserById(id);
    }

    public List<Room> findRoomByUser(User user) {
        return roomDAO.findRoomByUser(user);
    }

    public void save(Room room, User user1, User user2) {
        roomDAO.save(room);

        UserInRoomDAO userInRoomDAO = new UserInRoomDAOImp();

        userInRoomDAO.save(new UserInRoom(user1, room));
        userInRoomDAO.save(new UserInRoom(user2, room));


    }

    public void update(Room room) {
        roomDAO.update(room);
    }

    public void delete(Room room) {
        roomDAO.delete(room);
    }

    public List<Room> findRoomByName(String name) {
        return roomDAO.findRoomByName(name);
    }

    public void insertUsersInRoom(Room room, User user1, User user2) {
        roomDAO.insertUsersInRoom(room, user1, user2);
    }

}
