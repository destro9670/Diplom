package db.models;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @SequenceGenerator(name = "message_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "message_seq")
    private long id;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    @Column(name = "send_status", nullable = false)
    private boolean sendStatus;

    @Column(name = "send_date", nullable = false)
    private String sendDate;

    @Column(name = "system")
    private boolean system;

    @ManyToOne
    @JoinColumn(name = "user_from_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_to_id", nullable = false)
    private User taker;

    @ManyToOne
    @JoinColumn(name = "room_id" , nullable = false)
    private Room room;

    public Message() {
    }

    public Message(User sender,
                   User taker,
                   String body,
                   boolean readStatus,
                   boolean sendStatus,
                   String sendDate,
                   boolean system,
                   Room room) {

        this.sender = sender;
        this.taker = taker;
        this.body = body;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sendDate = sendDate;
        this.room = room;
        this.system = system;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getTaker() {
        return taker;
    }

    public void setTaker(User taker) {
        this.taker = taker;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean isSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(boolean sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Message{" +
                ", sender=" + sender.getNick() +
                ", taker=" + taker.getNick() +
                ", body='" + body + '\'' +
                ", readStatus=" + readStatus +
                ", sendStatus=" + sendStatus +
                ", sendDate='" + sendDate + '\'' +
                '}';
    }
}
