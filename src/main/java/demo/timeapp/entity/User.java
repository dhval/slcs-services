package demo.timeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 * Created by dhval on 1/23/16.
 */
@Entity
@Table(name = "person")
@Transactional
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fname", length = 50)
    String fname;

    @Column(name = "mname")
    String mname;

    @Column(name = "lname", length = 50)
    String lname;

    @Column(name = "email", length = 50)
    String email;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "password")
    String passwordHash;

    @Column(name = "role")
    String role;

    @Column
    Integer token;

    @Column
    Boolean isAccountNonExpired = false;

    @Column
    Boolean isAccountNonLocked = false;

    @Column
    Boolean isCredentialsNonExpired = false;

    @Column
    Boolean isEnabled = false;
/**
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserProject> projects;

    public List<UserProject> getProjects() {
        return projects;
    }

    public void setProjects(List<UserProject> projects) {
        this.projects = projects;
    }
**/
    @JsonIgnore
    public Long getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean hasUserRole() {
        return role.equalsIgnoreCase("user");
    }

    @JsonIgnore
    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return fname + " " + (mname==null ? "":mname) + " " + lname;
    }
}
