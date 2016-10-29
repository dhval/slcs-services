package demo.timeapp.dto;

import demo.timeapp.entity.Sheet;
import demo.timeapp.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhval on 7/3/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SheetStatus {

    private Long id;
    private User approver;
    private User user;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="America/New_York")
    private Date beginDt;
    String status;
    private Map<String, String> entries;

    public SheetStatus(Sheet sheet) {
        id = sheet.getId();
        approver = sheet.getApprover();
        user = sheet.getUser();
        beginDt = sheet.getBeginDt();
        entries = new HashMap<>();
        status = sheet.getStatus();
        sheet.getEntries().forEach(entry->entries.put(entry.getCode().toString(), Float.toString(entry.getTotal())));
    }

    public Long getId() {
        return id;
    }

    public User getApprover() {
        return approver;
    }

    public User getUser() {
        return user;
    }

    public Date getBeginDt() {
        return beginDt;
    }

    public Map getEntries() {
        return entries;
    }

    public String getStatus() {
        return status;
    }
}
