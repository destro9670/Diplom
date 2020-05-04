package db.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @SequenceGenerator(name = "message_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "message_seq")
    private long id;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    @Column(name = "send_status", nullable = false)
    private boolean sendStatus;

    @Column(name = "send_date", nullable = false)
    private String sendDate;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_from_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_to_id",nullable = false)
    private User taker;

    @ManyToOne
    @JoinColumn(name = "room_id" , nullable = false)
    private Room room;

    public Message() {
    }

    public Message(User sender,
                   User taker,
                   boolean readStatus,
                   boolean sendStatus,
                   String sendDate,
                   Room room) {

        this.sender = sender;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sendDate = sendDate;
        this.room = room;
        this.taker = taker;
    }

    public Message(User sender,
                   User taker,
                   String content,
                   boolean readStatus,
                   boolean sendStatus,
                   String sendDate,
                   Room room) {

        this.content = content;
        this.sender = sender;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sendDate = sendDate;
        this.room = room;
        this.taker = taker;
    }

    @Override
    public String toString() {
        return content;
    }
}
