package db.dao;

import db.models.Message;
import db.models.Room;
import db.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utiles.HibernateSessionFactoryUtil;

import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    @Override
    public Message findMessageById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .get(Message.class,id);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void save(Message message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(message);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update(Message message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(message);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void delete(Message message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(message);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<Message> findAllMessageBySender(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE sender = :user";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findAllMessageByTaker(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE taker = :user";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findUnsendedMessageTaker(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE (taker = :user AND sendStatus = false) ";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findUnreadedMessageTaker(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE taker = :user AND readStatus = false ";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findUnsendedMessageByTakerAndRoom(User user, Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE taker = :user AND sendStatus = false AND room =: room";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        query.setParameter("room", room);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findUnreadededMessageByTakerAndRoom(User user, Room room) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE taker = :user AND readStatus = false AND room =: room";
        Query query = session.createQuery(hql,Message.class);
        query.setParameter("user", user);
        query.setParameter("room", room);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

}
