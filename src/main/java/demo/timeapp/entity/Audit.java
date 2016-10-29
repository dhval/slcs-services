package demo.timeapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dhval on 4/26/16.
 */
@Entity
@Table
@Transactional
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "person_id")
    String userId;

    @Column
    String opcode;

    @Column(columnDefinition="timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    Date time;

    @Lob
    @Column(columnDefinition="TEXT", length=512)
    private String content;

    public Audit() {
    }

    public Audit(String userId, String opcode) {
        this.userId = userId;
        this.opcode = opcode;
        this.time = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
