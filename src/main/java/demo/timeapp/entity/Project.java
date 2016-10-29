package demo.timeapp.entity;

import demo.timeapp.util.DOBSerializer;
import demo.timeapp.validation.ProjectValid;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by dhval on 2/2/16.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "client"})})
@JsonInclude(JsonInclude.Include.NON_NULL)
@ProjectValid
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 25, message = "project.name.length")
    @NotBlank(message = "project.name.required")
    @Pattern(regexp = "^[0-9a-zA-Z _-]+$", message = "project.name.invalid")
    String name;

    @Column(nullable = false)
    @Length(min = 3, max = 25, message = "project.client.length")
    @Pattern(regexp = "^[0-9a-zA-Z _-]+$", message = "project.client.invalid")
    String client;

    @Column
    Boolean active = true;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Transient
    private User user;

    //@JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

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

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", client='" + client + '\'' +
                ", active=" + active +
                '}';
    }

    @Transient
    public String getLabel() {
        return new StringBuilder().append(name).append(" ( ").append(client).append(" )").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        return client != null ? client.equals(project.client) : project.client == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }
}
