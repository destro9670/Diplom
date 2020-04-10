import db.models.Message;
import db.models.User;
import org.junit.Assert;
import org.junit.Test;
import services.MessageServise;
import services.UserServise;

import java.util.List;

public class Database extends Assert {

    private static final String USER2 = "User{" +
            "name='" + "user2" + '\'' +
            ", surname='" + "user2" + '\'' +
            ", nick='" + "user2"  + '\'' +
            ", login='" + "user2" + '\'' +
            ", password='" + "user2"+ '\'' +
            ", keyDate='" + "user2" + '\'' +
            ", active=" + false +
            ", lastSeen='" + "user2" + '\'' +
            '}';

    private static final String USER1 = "User{" +
            "name='" + "user1" + '\'' +
            ", surname='" + "user1" + '\'' +
            ", nick='" + "user1"  + '\'' +
            ", login='" + "user1" + '\'' +
            ", password='" + "user1"+ '\'' +
            ", keyDate='" + "user1" + '\'' +
            ", active=" + false +
            ", lastSeen='" + "user1" + '\'' +
            '}';

    private static final String MESSAGE = "Message{" +
            ", sender=" + "user1" +
            ", taker=" + "user2" +
            ", body='" + "test" + '\'' +
            ", readStatus=" + false +
            ", sendStatus=" + true+
            ", sendDate='" + "11.02.15"+ '\'' +
            '}';

    @Test
    public void addUser(){
        UserServise servise = new UserServise();

        User user1 =new User("user1","user1","user1","user1",
                "user1","user1",false,"user1");

        User user2 =new User("user2","user2","user2","user2",
                "user2","user2",false,"user2");


        servise.save(user1);
        servise.save(user2);

        List<User> users1 = servise.findUserByNick("user1");
        List<User> users2 = servise.findUserByNick("user2");

        assertEquals(users1.get(0).toString(),USER1);
        assertEquals(users2.get(0).toString(),USER2);

        Message msg = new Message(users1.get(0),users2.get(0),"test",
                false,true,"11.02.15");

        MessageServise msgService = new MessageServise();

        msgService.save(msg);

        List<Message> messages1 = msgService.findAllMessageBySender(users1.get(0));
        List<Message> messages2 = msgService.findAllMessageByTaker(users2.get(0));

        assertEquals(messages1.get(0).toString(),MESSAGE);
        assertEquals(messages2.get(0).toString(),MESSAGE);
    }
}
