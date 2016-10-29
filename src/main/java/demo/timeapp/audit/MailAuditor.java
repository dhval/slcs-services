package demo.timeapp.audit;

import demo.timeapp.entity.Audit;
import demo.timeapp.entity.Sheet;
import demo.timeapp.repository.AuditRepository;
import demo.timeapp.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhval on 4/27/16.
 */
@Aspect
@Component
@Transactional
public class MailAuditor {
    private static final Logger LOG = LoggerFactory.getLogger(MailAuditor.class);

    @Autowired
    private AuditRepository repository;

    @AfterReturning("execution(* demo.timeapp.mail.MailService.sendInviteEmail(..))")
    public void sendInviteEmail(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        String email = (String) signatureArgs[0];
        Audit audit = new Audit(email, "audit.email.invite.success");
        repository.save(audit);
    }

    @AfterThrowing(value = "execution(* demo.timeapp.mail.MailService.sendInviteEmail(..))", throwing = "e")
    public void sendInviteEmailEx(JoinPoint thisJoinPoint, Exception e) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        String email = (String) signatureArgs[0];
        Audit audit = new Audit(email, "audit.email.invite.fail");
        audit.setContent(e.getMessage());
        repository.save(audit);
    }

    @AfterReturning("execution(* demo.timeapp.mail.MailService.sendTSheetAck(..))")
    public void sendTSheetAck(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        Sheet sheet = (Sheet) signatureArgs[0];
        Audit audit = new Audit(sheet.getUser().getEmail(), "audit.email.tsheet.ack.success");
        audit.setContent(StringUtil.toJSON(sheet));
        repository.save(audit);
    }

    @AfterThrowing("execution(* demo.timeapp.mail.MailService.sendTSheetAck(..))")
    public void sendTSheetAckEx(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        Sheet sheet = (Sheet) signatureArgs[0];
        Audit audit = new Audit(sheet.getUser().getEmail(), "audit.email.tsheet.ack.fail");
        audit.setContent(StringUtil.toJSON(sheet));
        repository.save(audit);
    }
}
