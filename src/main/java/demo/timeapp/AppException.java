package demo.timeapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceResourceBundle;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Created by dhval on 1/30/16.
 */
@XmlRootElement
public class AppException extends Exception implements Serializable {

    private static final long serialVersionUID = -7499519311228389443L;
    private String details = "";
    private String className = null;
    @XmlElement(name = "status")
    Integer status = 500;
    @XmlElement(name = "code")
    int code = -1;
    @XmlElement(name = "title")
    String title = "Error";
    @XmlElement(name = "content")
    String content = "Please contact administrator.";
    @XmlElement(name = "message")
    String message;

    public AppException() { }

    @Autowired
    MessageSourceResourceBundle bundle;
    /**
     *
     * @param status
     * @param code
     * @param message
     * @param title
     * @param content
     */
    public AppException(int status, int code, String message,
                        String title, String content) {
        super(message);
        this.status = status;
        this.code = code;
        this.title = title;
        this.content = content;
    }
    /**
     * Constructor.
     * @param e Exception
     */
    public AppException(Exception e) {
        super(e);
    }

    /**
     * Constructor.
     * @param details Exception details
     * @param e Exception
     * @param className Class name
     */
    public AppException(String details, Exception e, String className) {
        super(e);
        this.details = details;
        this.className = className;
    }

    public AppException(String errorMessage) {
        super(errorMessage);
        this.content = errorMessage;
    }

    public AppException(String errorCD,
                        String errorMessage) {
        super(errorMessage);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns exception details.
     * @return String - exception details
     */
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Returns exception class.
     * @return String - exception class name
     */
    public String getExceptionClass() {
        return className;
    }
}
