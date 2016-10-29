package demo.timeapp.controller.rest;

import demo.timeapp.entity.Audit;
import demo.timeapp.repository.AuditRepository;
import demo.timeapp.service.MessageBundle;
import demo.timeapp.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dhval on 9/22/16.
 */
@RestController
@RequestMapping("/public/audit")
public class AuditController extends AbstractController implements Rest {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    private static Set<String> auditFilter = new HashSet<>();

    @Autowired
    private AuditRepository repository;

    @Autowired
    MessageBundle bundle;

    static {
        auditFilter.add("audit.login.bad.credentials");
        auditFilter.add("audit.email.invite.success");
        auditFilter.add("audit.login.success");
        auditFilter.add("audit.demo.timeapp.controller.rest.SheetController.uploadSheet");
    }

    static class AuditObj {
        Date time;
        String userId;
        String msg;

        AuditObj (Audit audit) {
            time = audit.getTime();
            userId = audit.getUserId();
            msg = audit.getOpcode();
        }

        @JsonSerialize(using = DateTimeSerializer.class)
        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/today")
    @Transactional
    public List<AuditObj> listToday() {
        return repository.findByTimeGreaterThan(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)).stream()
                .map(AuditObj::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public List<AuditObj> list() {
        List<AuditObj> list = repository.findTopAudit().stream()
                .map(AuditObj::new).filter(auditObj -> auditFilter.contains(auditObj.getMsg()))
                .collect(Collectors.toList());
        list.forEach(auditObj -> auditObj.setMsg(bundle.getMessage(auditObj.getMsg())));
        return list;
    }

}
