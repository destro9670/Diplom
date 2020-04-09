package db.dao;

import db.models.Message;
import db.models.User;

import java.util.List;

public interface MessageDAO {

    Message findMessageById(long id);

    void save(Message message);

    void update(Message message);

    void delete(Message message);

    List<Message> findAllMessageBySender(User user);

    List<Message> findAllMessageByTaker(User user);

    List<Message> findUnsendedMessageTaker(User user);
}
