package demo.timeapp.entity;

import demo.timeapp.util.DOBSerializer;
import demo.timeapp.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


/**
 * Created by dhval on 2/1/16.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"person", "beginDt"})})
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class Sheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "approver", referencedColumnName = "id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "id")
    private User user;

    @Column
    String status;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "entry_id", referencedColumnName = "id")
    @OrderBy("sequence ASC")
    private List<Entry> entries;

    // The @Temporal annotation tells Hibernate if it should use a java.sql.Date or a java.sql.Timestamp to store the
    // date read from the database. Both extend the java.util.Date class, but java.sql.Date doesn't hold any time

    @Column(columnDefinition="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone="America/New_York", locale = "en_US")
    @Temporal(TemporalType.DATE)
    private Date beginDt;

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @JsonSerialize(using = DOBSerializer.class)
    public Date getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(Date beginDt) {
        this.beginDt = beginDt;
    }

    @Transient
    public List<String> getHeaders() {
        return DateUtil.getWeekDays(beginDt);
    }

    @Transient
    public boolean isSubmitted() {
        return status != null && (status.equals("submitted") || status.equals("approved"));
    }
}
