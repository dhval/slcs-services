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
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    String opcode;

    @Column(columnDefinition="timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    Date time;

    @Lob
    @Column(columnDefinition="TEXT", length=512)
    private String content;

    public Metrics() {
    }

    public Metrics(String opcode) {
        this.opcode = opcode;
        this.time = new Date();
    }

    public Long getId() {
        return id;
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
