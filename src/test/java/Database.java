import db.models.Message;
import db.models.Room;
import db.models.User;
import org.junit.*;
import services.datadase.MessageServise;
import services.datadase.RoomServise;
import services.datadase.UserServise;

import javax.persistence.Table;
import java.util.List;


public class Database extends Assert {

    private static UserServise userServise;
    private static RoomServise roomServise;
    private static MessageServise messageServise;


    @BeforeClass
    public static void start(){
        //init services
        userServise = new UserServise();
        roomServise = new RoomServise();
        messageServise = new MessageServise();

        //init users
        User user1 = new User("user1", "user1", "user1", "user1", "user1", "user1", false, "user1");
        User user2 = new User("user2", "user2", "user2", "user2", "user2", "user2", false, "user2");
        User user3 = new User("user3", "user3", "user3", "user3", "user3", "user3", false, "user3");
        User user4 = new User("user4", "user4", "user4", "user4", "user4", "user4", false, "user4");

        userServise.save(user1);
        userServise.save(user2);
        userServise.save(user3);
        userServise.save(user4);

        //init rooms
        Room  room1 = new Room(user1.getNick()+"_"+user2.getNick());
        Room  room2 = new Room(user1.getNick()+"_"+user3.getNick());
        Room  room3 = new Room(user1.getNick()+"_"+user4.getNick());
        Room  room4 = new Room(user2.getNick()+"_"+user3.getNick());
        Room  room5 = new Room(user2.getNick()+"_"+user4.getNick());
        Room  room6 = new Room(user3.getNick()+"_"+user4.getNick());

        roomServise.save(room1,user1,user2);
        roomServise.save(room2,user1,user3);
        roomServise.save(room3,user1,user4);
        roomServise.save(room4,user2,user3);
        roomServise.save(room5,user2,user4);
        roomServise.save(room6,user3,user4);

        //init messages
        Message msg11 = new Message(user1, user2, "message11", false, true, "message11",false,room1);
        Message msg12 = new Message(user2, user1, "message12", false, true, "message12",false,room1);
        Message msg13 = new Message(user1, user2, "message13", false, true, "message13",false,room1);
        Message msg14 = new Message(user2, user1, "message14", false, true, "message14",false,room1);

        Message msg21 = new Message(user1, user3, "message21", false, true, "message21",false,room2);
        Message msg22 = new Message(user3, user1, "message22", false, true, "message22",false,room2);
        Message msg23 = new Message(user1, user3, "message23", false, true, "message23",false,room2);
        Message msg24 = new Message(user3, user1, "message24", false, true, "message24",false,room2);

        Message msg31 = new Message(user1, user4, "message31", false, true, "message31",false,room3);
        Message msg32 = new Message(user4, user1, "message32", false, true, "message32",false,room3);
        Message msg33 = new Message(user1, user4, "message33", false, true, "message33",false,room3);
        Message msg34 = new Message(user4, user1, "message34", false, true, "message34",false,room3);

        Message msg41 = new Message(user2, user3, "message41", false, true, "message41",false,room4);
        Message msg42 = new Message(user3, user2, "message42", false, true, "message42",false,room4);
        Message msg43 = new Message(user2, user2, "message43", false, true, "message43",false,room4);
        Message msg44 = new Message(user3, user2, "message44", false, true, "message44",false,room4);

        Message msg51 = new Message(user2, user4, "message51", false, true, "message51",false,room5);
        Message msg52 = new Message(user4, user2, "message52", false, true, "message52",false,room5);
        Message msg53 = new Message(user2, user4, "message53", false, true, "message53",false,room5);
        Message msg54 = new Message(user4, user2, "message54", false, true, "message54",false,room5);

        Message msg61 = new Message(user3, user4, "message61", false, true, "message61",false,room6);
        Message msg62 = new Message(user4, user3, "message62", false, true, "message62",false,room6);
        Message msg63 = new Message(user3, user4, "message63", false, true, "message63",false,room6);
        Message msg64 = new Message(user4, user3, "message64", false, true, "message64",false,room6);

        messageServise.save(msg11);
        messageServise.save(msg12);
        messageServise.save(msg13);
        messageServise.save(msg14);

        messageServise.save(msg21);
        messageServise.save(msg22);
        messageServise.save(msg23);
        messageServise.save(msg24);

        messageServise.save(msg31);
        messageServise.save(msg32);
        messageServise.save(msg33);
        messageServise.save(msg34);

        messageServise.save(msg41);
        messageServise.save(msg42);
        messageServise.save(msg43);
        messageServise.save(msg44);

        messageServise.save(msg51);
        messageServise.save(msg52);
        messageServise.save(msg53);
        messageServise.save(msg54);

        messageServise.save(msg61);
        messageServise.save(msg62);
        messageServise.save(msg63);
        messageServise.save(msg64);
    }

    @Test
    public void findUsers() {
        User user = userServise.findUserByLogin("user1").get(0);
        assertEquals("user1",user.getNick());

        user = userServise.findUserByNick("user2").get(0);
        assertEquals("user2",user.getLogin());
    }

    @Test
    public void findMessage(){
        List<Message> messages = messageServise.findAllMessageBySender(userServise.findUserByNick("user1").get(0));

        for(Message message: messages){
            assertEquals("user1",message.getSender().getNick());
        }

        messages = messageServise.findAllMessageByTaker(userServise.findUserByNick("user1").get(0));

        for(Message message: messages){
            assertEquals("user1",message.getTaker().getNick());
        }

        messages = messageServise.findUnsendedMessageTaker(userServise.findUserByNick("user1").get(0));

        for(Message message: messages){
            assertEquals("user1",message.getSender().getNick());
            assertEquals(false,message.isSendStatus());
        }

    }

    @Test
    public void findRoom(){
        List<Room> rooms = roomServise.findRoomByUser(userServise.findUserByNick("user1").get(0));

        for (Room room: rooms) {
            //assertEquals(room.getName(),"user1_user"+i);
            System.out.println(room.getName());
        }
        //System.out.println("----------------------------------------");

    }





    @AfterClass
    public static void stop(){

    }
}
