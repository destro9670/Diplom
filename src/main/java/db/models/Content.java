package db.models;

import lombok.Data;
import org.junit.Test;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contents")
public class Content {

    @Id
    @SequenceGenerator(name = "content_seq", sequenceName = "contents_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "content_seq")
    private long id;

    @Column(name = "content",nullable = false)
    private String content;

    public Content(String content) {
        this.content = content;
    }

    public Content() {
    }
}
