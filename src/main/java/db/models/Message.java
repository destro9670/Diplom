package db.models;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User taker;

    @Column(name = "body")
    private String body;

    @Column(name = "read_status")
    private boolean readStatus;

    @Column(name = "send_status")
    private boolean sendStatus;

    @Column(name = "send_date")
    private String sendDate;
    ///TODO(1) create constructor for add new message into database
    public Message() {
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
}
