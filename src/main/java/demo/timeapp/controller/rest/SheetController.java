package demo.timeapp.controller.rest;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import demo.timeapp.AppException;
import demo.timeapp.aws.AWSClient;
import demo.timeapp.dto.SheetStatus;
import demo.timeapp.entity.Sheet;
import demo.timeapp.mail.MailService;
import demo.timeapp.repository.DBHelper;
import demo.timeapp.repository.ProjectRepository;
import demo.timeapp.repository.SheetRepository;
import demo.timeapp.service.MessageBundle;
import demo.timeapp.util.DateUtil;
import demo.timeapp.dto.CurrentUser;
import demo.timeapp.dto.FormSheet;
import demo.timeapp.repository.EntityRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dhval on 1/23/16.
 */

@RestController
@RequestMapping("/public/sheet")
public class SheetController extends AbstractController implements Rest {
    private static final Logger LOG = LoggerFactory.getLogger(SheetController.class);

    @Autowired
    private SheetRepository repository;

    @Autowired
    private EntityRepository sheetRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DBHelper db;

    @Autowired
    MessageBundle bundle;

    @Autowired
    AWSClient s3;

    @RequestMapping(value = "/findsheets", method = RequestMethod.POST)
    public List<Sheet> sheets(@ModelAttribute("user") CurrentUser user,
                              @RequestBody final FormSheet form) throws AppException {
        List<Sheet> sheets = db.findTSheet(form.getUser(), form.getStatus(), form.getBeginDate(), form.getEndDate());
        return sheets;
    }

    @RequestMapping(value = "/mysheets", method = RequestMethod.POST)
    public List<SheetStatus> findSheets(@ModelAttribute("user") CurrentUser user,
                                        @RequestBody final FormSheet form) throws AppException {
        List<SheetStatus> sheets = db.findTSheet(user.getUsername(), form.getStatus(), form.getBeginDate(),
                form.getEndDate()).stream().map(sheet-> new SheetStatus(sheet)).collect(Collectors.toList());
        return sheets;
    }

    @RequestMapping(value = "/savesheets", method = RequestMethod.POST)
    @Transactional
    public List<Sheet> saveSheets(@ModelAttribute("user") CurrentUser user,
                                  @RequestBody final List<Sheet> sheets) throws AppException {
        try {
            for (Sheet sheet : sheets) {
                db.hydrate(sheet);
                sheetRepository.create(sheet);
            }
        } catch (PersistenceException e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.database");
        }
        return sheets;
    }

    @RequestMapping(value = "/addsheet", method = RequestMethod.POST)
    @Transactional
    public Sheet addSheet(@ModelAttribute("user") CurrentUser user,
                          @RequestBody(required = false) final FormSheet form) throws AppException {
        Sheet sheet = (form == null || form.getBeginDate() == null) ?
                db.findLastByEmail(user.getUsername()) : db.findByEmailAndDate(user.getUsername(), form.getBeginDate());
        if (sheet.getProject() == null) {
            sheet.setProject(projectRepository.def());
        }
        return sheet;
    }
    /**

    @RequestMapping(value = "/sheet/{op}", method = RequestMethod.POST)
    @Transactional
    public Sheet saveSheet(@PathVariable("op") String opn, @ModelAttribute("user") CurrentUser user,
                           @RequestBody final Sheet sheet) throws AppException {
        try {
            LOG.info(user.toString() + " >> " + user.getUsername());
            switch(opn) {
                case "get":
                    return (sheet == null || sheet.getBeginDt() == null) ? db.findLastByEmail(user.getUsername())
                            : db.findByEmailAndDate(user.getUsername(), sheet.getBeginDt());
                case "save":
                    db.hydrate(sheet);
                    sheet.getEntries().forEach(Entry::calculate);
                    sheetRepository.create(sheet);
                    break;
                case "remove":
                    sheetRepository.remove(sheet);
                    break;
            }
        } catch (UnexpectedRollbackException e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.database");
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new AppException("error.database");
        }
        return sheet;
    }
     **/

    @RequestMapping(method = RequestMethod.GET)
    public Sheet find (@ModelAttribute("user") CurrentUser user,
            @RequestBody final Sheet sheet) throws AppException {
        return (sheet == null || sheet.getBeginDt() == null) ? db.findLastByEmail(user.getUsername())
                : db.findByEmailAndDate(user.getUsername(), sheet.getBeginDt());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<Map<String, String>> save(@ModelAttribute("user") CurrentUser user,
                                                      @RequestBody final Sheet sheet) throws AppException {
        db.hydrate(sheet);
        // sheet.getEntries().forEach(Entry::calculate);
        sheetRepository.create(sheet);
        return msg(0, "modified");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<Map<String, String>> delete(@ModelAttribute("user") CurrentUser user,
                                                      @RequestBody final Sheet sheet) throws AppException {
        repository.delete(sheet.getId());
        return msg(0, "deleted");
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Transactional
    public Sheet uploadSheet(@ModelAttribute("user") CurrentUser user,
                                     @RequestPart("timesheet") final Sheet sheet,
                                     @RequestPart("file") MultipartFile uploadfile) throws AppException {
        try {
            db.hydrate(sheet);
            sheet.setStatus("submitted");
            LOG.info(uploadfile.getName() + " - " + uploadfile.getContentType() + " - " +
                    uploadfile.getOriginalFilename() + " - " + uploadfile.getSize());
            // Save file locally
            String fileName = DateUtil.dateYYMMDD(sheet.getBeginDt()) + "-" + uploadfile.getOriginalFilename();
            String fileDir = System.getProperty("user.home") + "/timesheet/" + sheet.getUser().getEmail() + "/";
            final File fileOut = new java.io.File(fileDir, fileName);
            FileUtils.copyInputStreamToFile(uploadfile.getInputStream(), fileOut);
            sheetRepository.create(sheet);
            // AWS S3 upload
            s3.upload(fileDir + fileName,
                    "/timesheet/" + sheet.getUser().getEmail() + "/" + uploadfile.getOriginalFilename(),
                    CannedAccessControlList.PublicRead);
            // Email
            mailService.sendTSheetAck(sheet, fileDir, fileName);
        } catch (AppException ape) {
            throw ape;
        } catch (PersistenceException e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.database");
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new AppException("error.database");
        }

        return sheet;
    }

}

