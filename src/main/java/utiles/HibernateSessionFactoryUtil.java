package utiles;

import db.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jboss.logging.Logger;

public class HibernateSessionFactoryUtil {

    private static final Logger logger = Logger.getLogger(HibernateSessionFactoryUtil.class);

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil(){}

    public static SessionFactory getSessionFactory(){
        if(sessionFactory ==null){
            try{
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Message.class);
                configuration.addAnnotatedClass(Room.class);
                configuration.addAnnotatedClass(UserInRoom.class);
                configuration.addAnnotatedClass(Content.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return sessionFactory;
    }
}
