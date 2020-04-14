package db.dao;

import db.models.Room;
import db.models.User;
import db.models.UserInRoom;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utiles.HibernateSessionFactoryUtil;

import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public Room findUserById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .get(Room.class, id);
    }

    @Override
    public void save(Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(room);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(room);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(room);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Room> findRoomByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Room r WHERE r.name =: name";
        Query query = session.createQuery(hql);
        query.setParameter("name",name);
        List<Room> rooms = query.getResultList();
        transaction.commit();
        session.close();
        return rooms;
    }

    @Override
    public List<Room> findRoomByUser(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "SELECT uir.room FROM UserInRoom uir INNER JOIN User us ON (uir.user =: user) AND (us =: user)";
        Query query = session.createQuery(hql);
        query.setParameter("user", user);
        List<Room> rooms = query.list();
        transaction.commit();
        session.close();
        return rooms;
    }

    @Override
    public void insertUsersInRoom(Room room, User user1, User user2) {
        UserInRoomDAO userInRoomDAO = new UserInRoomDAOImp();

        userInRoomDAO.save(new UserInRoom(user1,room));
        userInRoomDAO.save(new UserInRoom(user2,room));
    }
}