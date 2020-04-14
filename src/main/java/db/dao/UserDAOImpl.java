package db.dao;

import db.models.Message;
import db.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utiles.HibernateSessionFactoryUtil;

import java.util.List;

@SuppressWarnings("ALL")
public class UserDAOImpl implements UserDAO {
    @Override
    public User findUserById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .get(User.class, id);
    }

    @Override
    public List<User> findUserByLogin(String login) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql ="FROM User WHERE login = :userLogin";
        Query query = session.createQuery(hql,User.class);
        query.setParameter("userLogin", login);
        List<User> users = query.list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public List<User> findUserByNick(String nick) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql ="FROM User WHERE nick = :userNick";
        Query query = session.createQuery(hql,User.class);
        query.setParameter("userNick", nick);
        List<User> users = query.list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Message> findAllUserMessage(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Message WHERE (sender = :user) OR (taker = :user)";
        Query query = session.createQuery(hql);
        query.setParameter("user", user);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

    @Override
    public List<Message> findAllNoTakedMessage(User user) {
        String hql = "FROM Message WHERE (taker = :user) AND (sendStatus = :status)";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setParameter("user", user);
        query.setParameter("status", false);
        List<Message> messages = query.list();
        transaction.commit();
        session.close();
        return messages;
    }

}

