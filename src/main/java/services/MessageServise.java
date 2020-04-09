package services;

import db.dao.MessageDAO;
import db.dao.MessageDAOImpl;
import db.models.Message;
import db.models.User;

import java.util.List;

public class MessageServise {

    private final MessageDAO  messageDAO;

    public MessageServise() {
        this.messageDAO = new MessageDAOImpl();
    }


    public Message findMessageById(long id){
        return messageDAO.findMessageById(id);
    }

    public void save(Message message){
        messageDAO.save(message);
    }

    void update(Message message){
        messageDAO.save(message);
    }

    void delete(Message message){
        messageDAO.delete(message);
    }

    List<Message> findAllMessageBySender(User user){
        return messageDAO.findAllMessageBySender(user);
    }

    List<Message> findAllMessageByTaker(User user){
        return messageDAO.findAllMessageByTaker(user);
    }

    List<Message> findUnsendedMessageTaker(User user){
        return messageDAO.findUnsendedMessageTaker(user);
    }
}
