package db.dao;

import db.models.Content;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utiles.HibernateSessionFactoryUtil;

public class ContentDAOImpl implements ContentDAO {
    @Override
    public Content findMessageById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .get(Content.class,id);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void save(Content message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(message);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update(Content message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(message);
        transaction.commit();
        session.close();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void delete(Content message) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(message);
        transaction.commit();
        session.close();
    }
}
