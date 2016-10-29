package demo.timeapp.audit;

import demo.timeapp.dto.CurrentUser;
import demo.timeapp.dto.FormUser;
import demo.timeapp.entity.Audit;
import demo.timeapp.entity.Project;
import demo.timeapp.entity.Sheet;
import demo.timeapp.repository.AuditRepository;
import demo.timeapp.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhval on 4/26/16.
 */
@Aspect
@Component
@Transactional
public class DBAuditor {
    private static final Logger LOG = LoggerFactory.getLogger(DBAuditor.class);

    @Autowired @Lazy
    private AuditRepository repository;

    @Before("execution(* demo.timeapp.controller.rest.UserController.createUser(..))")
    public void createUser(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        FormUser user = (FormUser) signatureArgs[0];
        CurrentUser currentUser = (CurrentUser) signatureArgs[1];
        Audit audit = new Audit(currentUser == null? "NA":currentUser.getUsername()
                , "audit.create.profile");
        audit.setContent(StringUtil.toJSON(user));
        repository.save(audit);
    }

    @Before("execution(* demo.timeapp.controller.rest.UserController.updateProfile(..))")
    public void profileUpdate(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        FormUser user = (FormUser) signatureArgs[0];
        CurrentUser currentUser = (CurrentUser) signatureArgs[1];
        Audit audit = new Audit(currentUser.getUsername(), "audit.update.profile");
        audit.setContent(StringUtil.toJSON(user));
        repository.save(audit);
    }

    @Before("execution(* demo.timeapp.controller.rest.ProjectController.*(..)) && args(user, project)")
    public void project(JoinPoint jp, CurrentUser user, Project project) {

        String userName = (user == null) ? "unknown" : user.getUsername();
        String code = "audit." + jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        Audit audit = new Audit(userName, code);
        audit.setContent(StringUtil.toJSON(project));
        LOG.info(code + " - " + audit.toString());
        repository.save(audit);
    }

    @Before("execution(* demo.timeapp.controller.rest.SheetController.*(..))")
    public void saveSheet(JoinPoint jp) {
        Object[] signatureArgs = jp.getArgs();
        CurrentUser user = (CurrentUser) signatureArgs[0];
        String userName = (user == null) ? "unknown" : user.getUsername();
        String code = "audit." + jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        if (signatureArgs.length >= 2 && (signatureArgs[1] instanceof Sheet)) {
            Sheet sheet = (Sheet) signatureArgs[1];
            if (StringUtils.isNotBlank(sheet.getStatus()) && !sheet.getStatus().equals("entered")) {
                code = "audit.demo.timeapp.controller.rest.SheetController.uploadSheet";
            }
        }
        Audit audit = new Audit(userName, code);
        audit.setContent(StringUtil.toJSON(signatureArgs[1]));
        repository.save(audit);
    }

    @AfterThrowing(pointcut = "execution(* demo.timeapp.repository.*.*(..))", throwing = "ex")
    public void handleException(DataIntegrityViolationException ex) {
        Audit audit = new Audit("audit.error", "audit.db.dataintegrity.error");
        audit.setContent(ex.getMessage());
        repository.save(audit);
    }

}
