package ua.gabz.dm.database.models;

import javax.persistence.*;

/**
 * Created by destr on 04.04.2020.
 */
@Entity
@Table(name = "MESSAGES")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private Users taker;

    @Column(name = "body")
    private String body;

    @Column(name = "read_status")
    private boolean readStatus;

    @Column(name = "send_status")
    private boolean sendStatus;

    @Column(name = "send_date")
    private String sendDate;

    public Messages() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getTaker() {
        return taker;
    }

    public void setTaker(Users taker) {
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
