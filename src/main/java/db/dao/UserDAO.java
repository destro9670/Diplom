package db.dao;

import db.models.Message;
import db.models.User;

import java.util.List;

public interface UserDAO {

    User findUserById(long id);

    List<User> findUserByLogin(String login);

    List<User> findUserByNick(String nick);

    void save(User user);

    void update(User user);

    void delete(User user);

    List<Message> findAllUserMessage(User user);

    List<Message> findAllNoTakedMessage(User user);

}
