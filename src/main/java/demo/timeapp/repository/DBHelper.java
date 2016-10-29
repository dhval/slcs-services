package demo.timeapp.repository;

import demo.timeapp.AppException;
import demo.timeapp.dto.ChargeCode;
import demo.timeapp.entity.Project;
import demo.timeapp.util.DateUtil;
import demo.timeapp.entity.Entry;
import demo.timeapp.entity.Sheet;
import demo.timeapp.entity.User;
import demo.timeapp.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by dhval on 4/16/16.
 */

@Service
public class DBHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DBHelper.class);

    @Autowired
    private MyRepository repository;

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("default.approver")
    private String defApprover;

    private void withProject(Sheet s) throws AppException{
        if (s.getProject() == null) {
            if (s.getUser() != null && StringUtils.isNotBlank(s.getUser().getEmail())) {
                Iterable<Project> projects = userRepository.findProject(s.getUser().getEmail());
                if (projects.iterator().hasNext()) {
                    s.setProject(projects.iterator().next());
                    return;
                }
            } else {
                s.setProject(projectRepository.def());
            }
        } else {
            Optional<Project> project = projectRepository.findTopByNameAndClient(s.getProject().getName(), s.getProject().getClient());
            if (project.isPresent()) s.setProject(project.get());
        }
    }

    private void withUser(Sheet s) throws AppException {
        User user = findUserByEmail(s.getUser().getEmail());
        if (user == null || !user.hasUserRole())
            throw new AppException("error.admin.cannot.create");
        s.setUser(user);
    }


    private void withApprover(Sheet s) throws AppException {
        String approverEmail = (s.getApprover() != null && s.getApprover().getEmail() != null) ?
                s.getApprover().getEmail() : "";
        User approver = findApproverByEmail(approverEmail);
        if (approver.hasUserRole())
            throw new AppException("error.user.cannot.approve");
        s.setApprover(approver);
    }

     public User findApproverByEmail(String email) {
        if (StringUtils.isBlank(email)) email = Constants.DEFAULT_APPROVER;
        User approver = findUserByEmail(email);
        return (approver==null) ?
                findUserByEmail(Constants.DEFAULT_APPROVER) : approver;
    }

    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public Sheet hydrate(Sheet s) throws AppException {
        withApprover(s);
        withProject(s);
        withUser(s);
        return s;
    }

    public Sheet findLastByEmail(String email) throws AppException {
        User user = findUserByEmail(email);
        Sheet sheet = sheetRepository.findTopByUserAndStatusOrderByBeginDtAsc(user, "entered");
        if (sheet != null) {
            withProject(sheet);
            withApprover(sheet);
            return sheet;
        }
        return addNewTSheet(email, Optional.empty());
    }

    public Sheet findByEmailAndDate(String email, Date date) throws AppException {
        LOG.info("Looking timesheet for " + email + " begining period " + date);
        User user = findUserByEmail(email);
        Sheet sheet = sheetRepository.findTopByUserAndBeginDt(user, date);
        if (sheet != null)
            return sheet;
        return addNewTSheet(email, Optional.of(date));
    }

    public List<Sheet> findTSheet(String email, String status, Date begin, Date end) throws AppException {
        User user = findUserByEmail(email);
        Date today = new Date();
        if (begin == null) {
            begin = DateUtil.firstDayOfYear();
            end = today;
        }
        if (end != null && end.compareTo(today) > 0) {
            end = today;
        }
        List<Sheet> sheets;
        if (StringUtils.isBlank(status))
            sheets = sheetRepository.findByUserAndBeginDtBetweenOrderByBeginDtDesc(user, begin, end);
        else
            sheets = sheetRepository.findByUserAndStatusAndBeginDtBetweenOrderByBeginDtDesc(user, status, begin, end);
        for(Sheet sheet : sheets) {
            // sanity checks, delete future Tsheet
        }
        return sheets;
    }

    private Sheet addNewTSheet(String userEmail, Optional<Date> optional) throws AppException {
        User user = findUserByEmail(userEmail);
        if (user == null || !user.hasUserRole())
            throw new AppException("error.admin.cannot.create");
        Date latest = null;
        // http://stackoverflow.com/questions/2937086/how-to-get-the-first-day-of-the-current-week-and-month
        // http://stackoverflow.com/questions/22890644/get-current-week-start-and-end-date-in-java-monday-to-sunday
        // http://stackoverflow.com/questions/2109145/how-to-get-first-day-of-a-given-week-number-in-java
        if (optional.isPresent()) {
            latest = optional.get();
        } else {
            latest = repository.findLastSheetByUser(user.getEmail());
            latest = (latest != null) ? latest : new Date();
        }
        // Make sure it is a Sunday
        latest = DateUtil.findSunday(latest).getTime();
        User approver = findApproverByEmail(defApprover);
        if (approver.hasUserRole())
            throw new AppException("error.user.cannot.approve");
        Sheet sheet = new Sheet();
        sheet.setApprover(approver);
        sheet.setUser(user);
        sheet.setBeginDt(latest);
        withProject(sheet);

        List<Entry> entries = new ArrayList<>();
        Entry regular = new Entry(ChargeCode.REGULAR);
        entityRepository.create(regular);
        Entry overtime = new Entry(ChargeCode.OVERTIME);
        entityRepository.create(overtime);
        Entry paid = new Entry(ChargeCode.PAID_TIMEOFF);
        entityRepository.create(paid);
        Entry unpaid = new Entry(ChargeCode.UNPAID_TIMEOFF);
        entityRepository.create(unpaid);
        entries.add(regular);
        entries.add(overtime);
        entries.add(paid);
        entries.add(unpaid);
        sheet.setEntries(entries);
        sheet = entityRepository.create(sheet);
        return sheet;
    }

    /**
    public Sheet addNewProject(Sheet sheet, Project project) throws AppException {
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setProject(project);
        entityRepository.create(timeEntry);
        List<TimeEntry> entries = sheet.getEntries();
        if (entries == null)
            entries = new ArrayList<>();
        entries.add(timeEntry);
        entityRepository.create(sheet);
        return sheet;
    }
   **/

}
