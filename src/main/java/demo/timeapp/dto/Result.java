package demo.timeapp.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by dhval on 1/30/16.
 */
@XmlRootElement
public class Result implements Serializable {

    private static final long serialVersionUID = 7974832012230420185L;

    @XmlElement
    private String key;
    @XmlElement
    private Object value;

    public Result(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
