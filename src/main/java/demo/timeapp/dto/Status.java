package demo.timeapp.dto;

/**
 * Created by dhval on 2/18/15.
 */
public class Status {
    private String code;
    private String description;
    private Object data;

    public Status() {
      this("0", "success");
    }

    public Status(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public Status(String code, String description, Object data) {
        this.code = code;
        this.description = description;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
