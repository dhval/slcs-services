package demo.timeapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by dhval on 4/2/16.
 */
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormSheet {

    public interface ValidateUserEmail {}

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date beginDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date endDate;
    String project;
    String client;
    String approver;
    @NotBlank(message = "user.email.blank" , groups = {Default.class, ValidateUserEmail.class})
    @Email(message = "user.email.invalid" , groups = {Default.class, ValidateUserEmail.class})
    String user;
    String status;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
