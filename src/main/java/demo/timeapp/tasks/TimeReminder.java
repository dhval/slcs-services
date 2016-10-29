package demo.timeapp.tasks;

import demo.timeapp.AppException;
import demo.timeapp.GmailService;
import demo.timeapp.entity.Sheet;
import demo.timeapp.entity.User;
import demo.timeapp.mail.Mail;
import demo.timeapp.mail.MailService;
import demo.timeapp.repository.DBHelper;
import demo.timeapp.repository.SheetRepository;
import demo.timeapp.repository.UserRepository;
import demo.timeapp.util.DateUtil;
import demo.timeapp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by dhval on 9/26/16.
 */
@Component
public class TimeReminder {

    private static final Logger LOG = LoggerFactory.getLogger(Health.class);
    private static final String PRE_REMINDER = "mail/time-reminder1";
    private static final String POST_REMINDER = "mail/time-reminder2";

    @Autowired
    @Lazy
    private DBHelper db;

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private SheetRepository sheetRepository;

    @Autowired
    @Lazy
    private MailService mailService;

    @Autowired
    @Lazy
    private GmailService gmailService;

    private ExecutorService reminderService;

    private List<Sheet> findUnSubmittedSheetsForPastWeek() {
        return userRepository.findAllActiveUsers().stream().map(user -> (sheetRepository.findTopByUserAndBeginDt(user, DateUtil.findLastSunday()))).filter(sheet -> sheet != null && !sheet.isSubmitted()).collect(Collectors.toList());
    }

    private void reminderMail(String template, String email, String userName, Date date) {
        try {
            Runnable runnableTask = () -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(30 * 1000);
                    LOG.info("Send reminder alert - " + email);
                    MimeMessage message = Mail.createHTMLEmail(email, "SLCS - Time sheet reminder", mailService.reminderMail(template, userName, date));
                    Mail.sendMessage(gmailService.getGmailService(), message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MessagingException me) {
                    LOG.warn("Reminder  failure - " + email, me);
                }  catch (Exception e) {
                    LOG.warn("Reminder  failure - " + email, e);
                }
            };
            reminderService.submit(runnableTask);
        }  catch (Exception e) {
            LOG.warn("Reminder  failure - " + email, e);
        }
    }

    // Every Monday 9 AM
    @Scheduled(cron = "0 30 4 * * FRI")
    // Given minute
    // @Scheduled(cron = "0 02 * * * *")
    @Transactional
    public void preTimeSheetReminder() {
        Date lastSunday = DateUtil.findLastSunday();
        try {
            LOG.info("Sending Initial Email Reminders: " + lastSunday);
            reminderService = Executors.newSingleThreadExecutor();
            List<Sheet> sheets = findUnSubmittedSheetsForPastWeek();
            sheets.forEach(sheet -> reminderMail(PRE_REMINDER, sheet.getUser().getEmail(), StringUtil.prettyName(sheet.getUser()), sheet.getBeginDt()));
            reminderService.shutdown();
        } catch (Exception e) {
            LOG.warn("Reminder  failure", e);
        }
    }

    // Every Monday 9 AM
    @Scheduled(cron = "0 0 9 * * MON")
    // Given minute
    //@Scheduled(cron = "0 21 * * * *")
    @Transactional
    public void postTimeSheetReminder() {
        Date lastSunday = DateUtil.findLastSunday();
        try {
            LOG.info("Sending Final Email Reminders: " + lastSunday);
            reminderService = Executors.newSingleThreadExecutor();
            List<Sheet> sheets = findUnSubmittedSheetsForPastWeek();
            sheets.forEach(sheet -> reminderMail(POST_REMINDER, sheet.getUser().getEmail(), StringUtil.prettyName(sheet.getUser()), sheet.getBeginDt()));
            reminderService.shutdown();
        } catch (Exception e) {
            LOG.warn("Reminder  failure", e);
        }
    }


}