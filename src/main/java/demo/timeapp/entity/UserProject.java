package demo.timeapp.entity;

import demo.timeapp.util.DOBSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

/**
 * Created by dhval on 7/27/16.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"person_id", "project_id"})})
@IdClass(UserProjectId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class UserProject {

    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="person_id", insertable = false, updatable =  false)
    private User user;

    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="project_id", insertable = false, updatable =  false)
    private Project project;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="America/New_York", locale = "en_US")
    @Temporal(TemporalType.DATE)
    private Date created;

    @JsonSerialize(using = DOBSerializer.class)
    public Date getCreated() {
        return created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

@Embeddable
class UserProjectId implements Serializable {

    Long user;

    Long project;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user.getId();
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public void setProject(Project project) {
        this.project = project.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProjectId that = (UserProjectId) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return project != null ? project.equals(that.project) : that.project == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
}
