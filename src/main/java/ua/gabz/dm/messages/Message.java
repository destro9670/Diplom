package ua.gabz.dm.messages;

public class Message implements IMessage {

    private String jsonMessage;

    public Message(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    public String getTextMessage() {
        return null;
    }
}
