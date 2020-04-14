package services.datadase;

import db.dao.UserDAO;
import db.dao.UserDAOImpl;
import db.models.Message;
import db.models.User;

import java.util.List;

public class UserServise {

    private final UserDAO userDAO;

    public UserServise() {
        this.userDAO = new UserDAOImpl();
    }

    public User findUserById(long id){
        return userDAO.findUserById(id);
    }

    public void save(User user){
        userDAO.save(user);
    }


    public void update(User user){
        userDAO.update(user);
    }

    public void delete(User user){
        userDAO.delete(user);
    }

    public List<Message> findAllUserMessage(User user){
        return userDAO.findAllUserMessage(user);
    }

    public List<Message> findAllNoTakedMessage(User user){
        return userDAO.findAllNoTakedMessage(user);
    }

    public List<User> findUserByLogin(String login){
        return userDAO.findUserByLogin(login);
    }

    public List<User> findUserByNick(String nick){
        return userDAO.findUserByNick(nick);
    }

}
