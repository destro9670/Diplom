package services.communication;

import db.models.Room;
import db.models.User;
import messages.ClientMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.enums.MessageStatus;
import messages.enums.MessageType;
import messages.enums.SubType;
import messages.SystemMessage;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import services.datadase.MessageServise;
import services.datadase.RoomServise;
import services.datadase.UserServise;
import utiles.ClientHolderUtil;

import java.util.List;

public class MessageAnalyserService {

    private final static Logger logger = Logger.getLogger(MessageAnalyserService.class);
    private final UserServise userServise;
    private final MessageServise messageServise;
    private final RoomServise roomServise;
    private final CommutationService commutationService;
    private final User sender;
    private final User system;


    public MessageAnalyserService(User sender) {
        userServise = new UserServise();
        messageServise = new MessageServise();
        roomServise = new RoomServise();
        commutationService = new CommutationService();
        this.sender = sender;

        system = userServise.findUserById(1);
    }


    public void analyze(Message message) throws JSONException {

        JSONObject msg = new JSONObject(message.getTextMessage());
        String type = msg.getString("Type");
        String subType = msg.getString("SubType");
        String messageType = msg.getString("MessageType");

        ///client requests
        if (messageType.equals("Room")) {

            if (type.equals("Request")) {
                if (subType.equals("Put")) {
                    //create room request
                    createRoom(msg);
                }

                if (subType.equals("post")) {
                    JSONObject body = msg.getJSONObject("Body");

                    //in or out room
                    String action = body.getString("Action");

                    if (action.equals("in")) {

                        List<Room> room = roomServise.findRoomByName(msg.getJSONObject("Body").getString("Room"));

                        if (!room.isEmpty()) {
                            commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.POST, MessageType.ROOM, "NoSuchRoom"), false);

                            if (ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).getInRoom().equals("NuN")) {
                                maskAllMessageReaded(roomServise.findRoomByName(
                                        body.getString("Room")));

                                ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).setInRoom(body.getString("Room"));

                                chackActionMessage(message, msg, room);

                            } else {
                                commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.POST, MessageType.ROOM, "Another Room is opened"), false);
                            }
                        } else {
                            commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.POST, MessageType.ROOM, "NoSuchRoom"), false);
                        }
                    } else if (action.equals("Out")) {

                        List<Room> room = roomServise.findRoomByName(msg.getJSONObject("Body").getString("Room"));

                        if (!room.isEmpty()) {
                            commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.POST, MessageType.ROOM, "NoSuchRoom"), false);
                            return;
                        }

                        if (!ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).getInRoom().equals("NuN")) {

                            ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).setInRoom("NuN");

                            chackActionMessage(message, msg, room);

                        } else {
                            commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.POST, MessageType.ROOM, "Another Room is opened"), false);
                        }

                    }
                }
            }

        }
        if (messageType.equals("Message")) {

            if (type.equals("Request")) {
                if (subType.equals("Put")) {
                    JSONObject body = msg.getJSONObject("Body");

                    String from = body.getString("From");
                    if (from.equals(sender.getNick())) {
                        List<User> takers = userServise.findUserByNick(body.getString("To"));
                        if (takers.isEmpty()) {
                            commutationService.sendSystemMessageToUser(sender, system,
                                    new ErrorMessage(SubType.PUT, MessageType.ROOM, "No Such Intrlocutor"), false);
                        }

                        User taker = takers.get(0);

                        List<Room> rooms1 = roomServise.findRoomByName(sender.getNick() + "_" + taker.getNick());
                        if (rooms1.isEmpty())
                            rooms1 = roomServise.findRoomByName(taker.getNick() + "_" + sender.getNick());
                        if (rooms1.isEmpty()) {
                            commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.PUT, MessageType.MESSAGE, "NoSuchRoom"), false);
                        }

                        db.models.Message dbMessage = new db.models.Message(sender, taker, false, false, body.getString("Date"), rooms1.get(0));

                        dbMessage.setContent(msg.toString());

                        commutationService.sendClientMessageToUser(taker, dbMessage, false);

                    } else {
                        commutationService.sendSystemMessageToUser(sender, system, new ErrorMessage(SubType.PUT, MessageType.MESSAGE, "NoSuchIntrlocutor"), false);
                    }
                    //13
                }
            }
        }

        if (messageType.equals("Stream")) {
            if (type.equals("Info")) {
                if (subType.equals("post")) {
                    JSONObject body = msg.getJSONObject("Body");
                    if (body.getString("User").equals(sender.getNick())) {
                        if (body.getString("Action").equals("Close")) {
                            ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).closeThread();
                            List<Room> rooms = roomServise.findRoomByUser(sender);

                            for (Room room : rooms) {
                                User taker;

                                if (sender != room.getUsers().get(0))
                                    taker = room.getUsers().get(0);
                                else taker = room.getUsers().get(1);

                                db.models.Message dbMessage = new db.models.Message(sender, taker, false, false, body.getString("Data"), room);

                                JSONObject bodyResponse = new JSONObject().put("Action", "Disconnected")
                                        .put("User", sender.getNick());
                                dbMessage.setContent(new JSONObject().put("Type", "Info")
                                        .put("SubType", "Post")
                                        .put("MessageType", "Client")
                                        .put("Body", bodyResponse).toString());
                                commutationService.sendClientMessageToUser(taker, dbMessage, false);
                            }

                        }
                    }
                }
            }
        }
    }

    private void chackActionMessage(Message message, JSONObject msg, List<Room> room) throws JSONException {
        commutationService.sendSystemMessageToUser(sender, system, new ClientMessage(msg.put("Status", "OK")), false);

        List<User> users = room.get(0).getUsers();
        User taker = null;
        if (users.get(0) != system && users.get(0) != sender) {
            taker = users.get(0);
        } else {
            if (users.get(1) != system && users.get(1) != sender) {
                taker = users.get(1);
            }
        }

        commutationService.sendSystemMessageToUser(taker, system, message, false);
    }

    private void maskAllMessageReaded(List<Room> rooms) {
        Room room = rooms.get(0);

        List<db.models.Message> messages = messageServise.findUnreadededMessageByTakerAndRoom(sender, room);

        for (db.models.Message msg : messages) {
            msg.setReadStatus(true);
            messageServise.update(msg);
        }
    }


    private void createRoom(JSONObject msg) throws JSONException {

        logger.info(sender.getNick() + "try to create room");
        //get users Nick
        String strCreator = msg.getJSONObject("Body")
                .getString("Creator");

        String strInterlocutor = msg.getJSONObject("Body")
                .getString("Interlocutor");

        ///get Users Obj from datadase
        logger.info(msg.toString());
        List<User> creators = userServise.findUserByNick(strCreator);
        List<User> interlocutors = userServise.findUserByNick(strInterlocutor);
        if (creators.isEmpty() || interlocutors.isEmpty()) {
            commutationService.sendSystemMessageToUser(sender, system,
                    new ErrorMessage(SubType.PUT, MessageType.ROOM, "No Such Intrlocutor"), false);
        }

        //check if created room exist
        boolean room1 = roomServise.findRoomByName(strCreator + "_" + strInterlocutor).isEmpty();
        boolean room2 = roomServise.findRoomByName(strInterlocutor + "_" + strCreator).isEmpty();
        if (!room1 || !room2) {
            commutationService.sendSystemMessageToUser(sender, system,
                    new ErrorMessage(SubType.PUT, MessageType.ROOM, "Current Room Already Exist"), false);
        } else {

            //create room
            Room room = new Room(strCreator + "_" + strInterlocutor);

            roomServise.save(room, creators.get(0), interlocutors.get(0));

            //info sender of success
            commutationService.sendSystemMessageToUser(sender, system,
                    new SystemMessage(SubType.PUT, MessageType.ROOM, MessageStatus.OK,
                            new JSONObject().put("Creator", strCreator)
                                    .put("Interlocutor", strInterlocutor)), false);
            //sending info of creating room for interlocutor
            JSONObject interlocutorStatusInfo = new JSONObject().put("User", strInterlocutor);

            if (ClientHolderUtil.getInstance().contains(interlocutors.get(0).getNick())) {
                interlocutorStatusInfo.put("UserStatus", "Online");
            } else {
                interlocutorStatusInfo.put("UserStatus", "Ofline");
                interlocutorStatusInfo.put("LastSeen", interlocutors.get(0).getLastSeen());
            }

            commutationService.sendSystemMessageToUser(sender, system,
                    new SystemMessage(SubType.POST, MessageType.CLIENT, MessageStatus.NULL,
                            interlocutorStatusInfo), false);

            commutationService.sendSystemMessageToUser(interlocutors.get(0), creators.get(0),
                    new SystemMessage(SubType.PUT, MessageType.ROOM, MessageStatus.NULL,
                            new JSONObject().put("Creator", strCreator)
                                    .put("Interlocutor", strInterlocutor)), false);
        }
    }

    public void sendUnsendedMessages() {

        List<Room> rooms = roomServise.findRoomByUser(sender);

        Room systemRoom = roomServise.findRoomByName("system_" + sender.getNick()).get(0);

        List<db.models.Message> messages = messageServise.findUnsendedMessageTaker(sender);


        if (!messages.isEmpty()) {

            List<db.models.Message> systemMessages = messageServise.findUnsendedMessageByTakerAndRoom(sender, systemRoom);

            for (db.models.Message message : systemMessages) {
                message.setSendStatus(true);
                message.setSendStatus(true);
                messageServise.update(message);
                commutationService.sendClientMessageToUser(sender, message, true);
            }

            for (Room room : rooms) {
                messages = messageServise.findUnsendedMessageByTakerAndRoom(sender, room);

                for (db.models.Message message : messages) {
                    message.setReadStatus(true);
                    message.setSendStatus(true);
                    messageServise.update(message);
                    commutationService.sendClientMessageToUser(sender, message, true);
                }
            }
        }
    }
}
