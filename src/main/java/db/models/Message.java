package db.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    @NotNull
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    @NotNull
    private User taker;

    @Column(name = "body")
    @NotNull
    private String body;

    @Column(name = "read_status")
    @NotNull
    private boolean readStatus;

    @Column(name = "send_status")
    @NotNull
    private boolean sendStatus;

    @Column(name = "send_date")
    @NotNull
    private String sendDate;

    public Message() {
    }

    public Message(@NotNull User sender,
                   @NotNull User taker,
                   @NotNull String body,
                   @NotNull boolean readStatus,
                   @NotNull boolean sendStatus,
                   @NotNull String sendDate) {

        this.sender = sender;
        this.taker = taker;
        this.body = body;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sendDate = sendDate;
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
