package db.dao;

import db.models.Room;
import db.models.User;
import db.models.UserInRoom;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utiles.HibernateSessionFactoryUtil;

import java.util.List;

public class UserInRoomDAOImp implements UserInRoomDAO {
    @Override
    public List<Room> findRoomByUser(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql ="SELECT Room FROM UserInRoom WHERE User= :user";
        Query query = session.createQuery(hql,UserInRoom.class);
        query.setParameter("user", user);
        List<Room> rooms= query.list();
        transaction.commit();
        session.close();
        return rooms;
    }

    @Override
    public List<User> findUserByRoom(Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql ="SELECT User FROM UserInRoom WHERE Room= :room";
        Query query = session.createQuery(hql,UserInRoom.class);
        query.setParameter("room", room);
        List<User> users = query.list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public void save(UserInRoom userInRoom) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(userInRoom);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Room room) {
        List<User> users=findUserByRoom(room);

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i <users.size() ; i++) {
            session.delete(new UserInRoom(users.get(0),room));
            transaction.commit();
        }
        session.close();
    }
}
