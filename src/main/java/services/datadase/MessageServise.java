package services.datadase;

import db.dao.ContentDAO;
import db.dao.ContentDAOImpl;
import db.dao.MessageDAO;
import db.dao.MessageDAOImpl;
import db.models.Content;
import db.models.Message;
import db.models.Room;
import db.models.User;

import java.util.List;

public class MessageServise {

    private final MessageDAO messageDAO;
    private final ContentDAO contentDAO;

    public MessageServise() {
        this.contentDAO = new ContentDAOImpl();
        this.messageDAO = new MessageDAOImpl();
    }


    public Message findMessageById(long id) {
        return messageDAO.findMessageById(id);
    }

    public void save(Message message, Content content) {
        contentDAO.save(content);
        messageDAO.save(message);
    }

    public void update(Message message) {
        messageDAO.save(message);
    }

    public void delete(Message message) {
        messageDAO.delete(message);
    }

    public List<Message> findAllMessageBySender(User user) {
        return messageDAO.findAllMessageBySender(user);
    }

    public List<Message> findAllMessageByTaker(User user) {
        return messageDAO.findAllMessageByTaker(user);
    }

    public List<Message> findUnsendedMessageTaker(User user) {
        return messageDAO.findUnsendedMessageTaker(user);
    }

    public List<Message> findUnreadedMessageTaker(User user) {
        return messageDAO.findUnreadedMessageTaker(user);
    }

    public List<Message> findUnsendedMessageByTakerAndRoom(User user, Room room) {
        return messageDAO.findUnsendedMessageByTakerAndRoom(user, room);
    }

    public List<Message> findUnreadededMessageByTakerAndRoom(User user, Room room) {
        return messageDAO.findUnreadededMessageByTakerAndRoom(user, room);
    }
}