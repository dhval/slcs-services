package demo.timeapp.mail;

import demo.timeapp.AppException;
import demo.timeapp.GmailService;
import demo.timeapp.entity.Sheet;
import demo.timeapp.service.MessageBundle;
import demo.timeapp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private GmailService gmailService;

    @Autowired
    MessageBundle bundle;

    public String createTokenMail(String token) throws MessagingException {
        final Context ctx = new Context();
        ctx.setVariable("token", token);
        final String htmlContent = this.templateEngine.process("mail/forgotpwd", ctx);
        return htmlContent;
    }

    public String createInviteMail(String userName, String password) throws MessagingException {
        final Context ctx = new Context();
        ctx.setVariable("userName", userName);
        ctx.setVariable("password", password);
        final String htmlContent = this.templateEngine.process("mail/invite", ctx);
        return htmlContent;
    }

    public String reminderMail(String template, String userName, Date date) throws MessagingException {
        final Context ctx = new Context();
        ctx.setVariable("userName", userName);
        ctx.setVariable("timeDate", DateUtil.dateYYMMDD(date));
        final String htmlContent = this.templateEngine.process(template, ctx);
        return htmlContent;
    }

    public String createHTMLSheet(Map<String, Object> map) throws MessagingException {
        final Context ctx = new Context();
        ctx.setVariable("map", map);
        final String htmlContent = this.templateEngine.process("mail/sheet", ctx);
        return htmlContent;
    }

    public void sendInviteEmail(String email, String userName, String password)
            throws AppException {
        try {
            MimeMessage message = Mail.createHTMLEmail(email,
                    bundle.getMessage("mail.welcome"), createInviteMail(userName, password));
            Mail.sendMessage(gmailService.getGmailService(), message);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.email");
        }
    }

    public void sendTSheetAck(Sheet sheet, String fileDir, String fileName)
            throws AppException {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("timesheets", sheet.getEntries());
            map.put("DATE", DateUtil.dateYYMMDD(sheet.getBeginDt()));
            String mailTxt = createHTMLSheet(map);
            MimeMessage message = Mail.createEmailWithAttachment(sheet.getUser().getEmail(),
                    bundle.getMessage("mail.your.timesheet"), mailTxt, fileDir, fileName);
            Mail.sendMessage(gmailService.getGmailService(), message);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.email");
        }

    }
}