package services.communication;

import db.models.Content;
import db.models.Room;
import db.models.User;
import messages.ClientMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.enums.MessageStatus;
import messages.enums.MessageType;
import messages.enums.SubType;
import messages.SystemMessage;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import services.datadase.MessageServise;
import services.datadase.RoomServise;
import services.datadase.UserServise;
import utiles.ClientHolderUtil;

import java.util.List;

public class MessageAnalyserService {

    private static final String DISCONNECTED_USER_MESSAGE_HEADER =
            "{" +
                    "\"Type\":\"Info\"," +
                    "\"SubType\":\"Post\",\n" +
                    "\"MessageType\":\"Client\"" +
                    "}";


    private final static Logger logger = Logger.getLogger(MessageAnalyserService.class);
    private final UserServise userServise;
    private final MessageServise messageServise;
    private final RoomServise roomServise;
    private final CommutationService commutationService;
    private final User sender;
    private final User system;


    public MessageAnalyserService(User sender){
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

            if(type.equals("Request")){
                if(subType.equals("Put")){
                    //create room request
                    createRoom(msg);
                }

                if(subType.equals("post")){
                    JSONObject body = msg.getJSONObject("Body");

                    //in or out room
                    String action = body.getString("Action");

                    if(action.equals("in")){

                        if(ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).getInRoom().equals("NuN")) {
                            maskAllMessageReaded(roomServise.findRoomByName(
                                    body.getString("Room")));

                            ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).setInRoom(body.getString("Room"));

                            commutationService.sendSystemMessageToUser(sender, system, new ClientMessage(msg.put("Status", "OK")));
                        }else{
                            commutationService.sendSystemMessageToUser(sender,system,new ErrorMessage(SubType.POST,MessageType.ROOM,"Another Room is opened"));
                        }
                    }else if (action.equals("Out")) {
                        if(!ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).getInRoom().equals("NuN")) {

                            ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).setInRoom("NuN");

                            commutationService.sendSystemMessageToUser(sender, system, new ClientMessage(msg.put("Status", "OK")));

                        }else{
                            commutationService.sendSystemMessageToUser(sender,system,new ErrorMessage(SubType.POST,MessageType.ROOM,"Another Room is opened"));
                        }

                    }
                }
            }

        }
        if (messageType.equals("Message")) {

            if(type.equals("Request")){
                if(subType.equals("Put")){
                    JSONObject body = msg.getJSONObject("Body");

                    String from = body.getString("From");
                    if(from.equals(sender.getNick())){
                        User taker = userServise.findUserByNick(msg.getString("To")).get(0);

                        List<Room> rooms1 = roomServise.findRoomByName(sender.getNick()+"_"+taker.getNick());
                        if(rooms1.isEmpty())
                            rooms1 = roomServise.findRoomByName(taker.getNick()+"_"+sender.getNick());
                        if (rooms1.isEmpty()){
                            commutationService.sendSystemMessageToUser(sender,system,new ErrorMessage(SubType.PUT,MessageType.MESSAGE,"NoSuchRoom"));
                        }

                        db.models.Message dbMessage = new db.models.Message(sender,taker,false,false,body.getString("Data"),rooms1.get(0));

                        Content content = new Content(body.getString("Msg"));

                        commutationService.sendClientMessageToUser(taker,dbMessage,content,false);

                    }else{
                        commutationService.sendSystemMessageToUser(sender,system,new ErrorMessage(SubType.PUT,MessageType.MESSAGE,"NoSuchIntrlocutor"));
                    }
                    //13
                }
            }
        }

        if (messageType.equals("Stream")) {
            if(type.equals("Info")){
                if (subType.equals("post")){
                    JSONObject body = msg.getJSONObject("Body");
                    if(body.getString("User").equals(sender.getNick())){
                        if(body.getString("Action").equals("Close")){
                            ClientHolderUtil.getInstance().getOnlineClient(sender.getNick()).closeThread();
                            List<Room> rooms = roomServise.findRoomByUser(sender);

                            for (Room room: rooms ) {
                                User taker ;

                                if(sender!=room.getUsers().get(0))
                                    taker = room.getUsers().get(0);
                                else  taker = room.getUsers().get(1);

                                db.models.Message dbMessage = new db.models.Message(sender,taker,false,false,body.getString("Data"),room);

                                JSONObject bodyResponse = new JSONObject().put("Action","Disconnected")
                                        .put("User",sender.getNick());
                                Content content = new Content(new JSONObject(DISCONNECTED_USER_MESSAGE_HEADER).put("Body",bodyResponse).toString());
                                commutationService.sendClientMessageToUser(taker,dbMessage,content,false);
                            }

                        }
                    }
                }
            }
        }
    }

    private void maskAllMessageReaded(List<Room> rooms) {
        Room room = rooms.get(0);

        List<db.models.Message> messages = messageServise.findUnreadededMessageByTakerAndRoom(sender,room);

        for (db.models.Message msg:messages) {
            msg.setReadStatus(true);
            messageServise.update(msg);
        }
    }


    private void createRoom(JSONObject msg) throws JSONException {
        //get users Nick
        String strCreator = msg.getJSONObject("Body")
                .getString("Creator");

        String strInterlocutor = msg.getJSONObject("Body")
                .getString("Creator");

        ///get Users Obj from datadase
        User  creator = userServise.findUserByNick(strCreator).get(0);
        User  interlocutor = userServise.findUserByNick(strInterlocutor).get(0);
        if(creator == null || interlocutor == null) {
            commutationService.sendSystemMessageToUser(sender,system,
                    new ErrorMessage(SubType.PUT, MessageType.ROOM,"No Such Intrlocutor"));
        }

        //check if created room exist
        Room room1 = roomServise.findRoomByName(strCreator + "_" + strInterlocutor).get(0);
        Room room2 = roomServise.findRoomByName(strInterlocutor+ "_" + strCreator).get(0);
        if(room1 != null || room2 != null) {
            commutationService.sendSystemMessageToUser(sender,system,
                    new ErrorMessage(SubType.PUT, MessageType.ROOM,"Current Room Already Exist"));
        }

        //create room
        Room room = new Room(strCreator + "_" + strInterlocutor);

        roomServise.save(room,creator,interlocutor);

        //info sender of success
        commutationService.sendSystemMessageToUser(sender,system,
                new SystemMessage(SubType.PUT,MessageType.ROOM, MessageStatus.OK,
                        new JSONObject().put("Creator",strCreator)
                                .put("Interlocutor",strInterlocutor)));
        //sending info of creating room for interlocutor
        JSONObject interlocutorStatusInfo = new JSONObject().put("User",strInterlocutor);

        if(ClientHolderUtil.getInstance().contains(interlocutor.getNick())){
            interlocutorStatusInfo.put("UserStatus","Online");
        }else{
            interlocutorStatusInfo.put("UserStatus","Ofline");
            interlocutorStatusInfo.put("LastSeen",interlocutor.getLastSeen());
        }

        commutationService.sendSystemMessageToUser(sender,system,
                new SystemMessage(SubType.POST,MessageType.CLIENT,MessageStatus.NULL,
                        interlocutorStatusInfo));

        commutationService.sendSystemMessageToUser(interlocutor,creator,
                new SystemMessage(SubType.PUT,MessageType.ROOM,MessageStatus.OK,
                        new JSONObject().put("Creator",strCreator)
                                .put("Interlocutor",strInterlocutor)));
    }

    public void sendUnsendedMessages() {

        List<Room> rooms = roomServise.findRoomByUser(sender);

        Room systemRoom = roomServise.findRoomByName("system_"+sender.getNick()).get(0);

        List<db.models.Message> messages = messageServise.findUnsendedMessageTaker(sender);


        if(!messages.isEmpty()) {

            List<db.models.Message> systemMessages = messageServise.findUnsendedMessageByTakerAndRoom(sender,systemRoom);

            for (db.models.Message message : systemMessages) {
                commutationService.sendClientMessageToUser(sender, message, message.getContent(), true);
            }

            for (Room room : rooms) {
                messages = messageServise.findUnsendedMessageByTakerAndRoom(sender, room);

                for (db.models.Message message : messages) {
                    commutationService.sendClientMessageToUser(sender, message, message.getContent(), true);
                }
            }
        }
    }
}
