package demo.timeapp.dto;

import demo.timeapp.validation.AlphaNumChars;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * Created by dhval on 1/24/16.
 */
public class FormUser {
    public interface ValidateUserEmail {}

    @NotBlank(message = "user.first.blank")
    @AlphaNumChars(message = "user.first.alpha")
    @Size(message = "user.first.size", min = 3, max = 25)
    String fname;
    @AlphaNumChars(message = "user.middle.alpha")
    String mname;
    @NotBlank(message = "user.last.blank")
    @AlphaNumChars(message = "user.last.alpha")
    @Size(message = "user.last.size", min = 3, max = 25)
    String lname;
    @NotBlank(message = "user.email.blank" , groups = {Default.class, ValidateUserEmail.class})
    @Email(message = "user.email.invalid" , groups = {Default.class, ValidateUserEmail.class})
    String email;
    String role;
    String password;
    String projectId;
    Boolean isAccountNonLocked = false;
    Boolean isEnabled = false;

    public FormUser() {
    }

    public FormUser(String fname, String lname, String email, String role, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.role = role;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
