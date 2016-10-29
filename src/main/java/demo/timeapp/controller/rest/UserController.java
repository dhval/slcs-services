package demo.timeapp.controller.rest;

import demo.timeapp.AppException;
import demo.timeapp.dto.CurrentUser;
import demo.timeapp.dto.FormUser;
import demo.timeapp.dto.ListItem;
import demo.timeapp.dto.Status;
import demo.timeapp.entity.Project;
import demo.timeapp.entity.User;
import demo.timeapp.entity.UserProject;
import demo.timeapp.mail.MailService;
import demo.timeapp.repository.*;
import demo.timeapp.service.MessageBundle;
import demo.timeapp.service.UserService;
import demo.timeapp.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dhval on 4/25/16.
 */
@RestController
public class UserController extends AbstractController implements Rest {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private MyRepository repository;

    @Autowired
    private MailService mailService;

    @Autowired
    MessageBundle bundle;

    @Value("${default.password}")
    private String defPassword;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<ListItem> list() {
        List<User> users = repository.findAllUsers();
        return users.stream().map(user ->  new ListItem(user.toString(), user.getEmail())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User findUser(@RequestBody @Validated({FormUser.ValidateUserEmail.class}) final FormUser form) {
        User user = userService.getUserByEmail(form.getEmail());
        return user;
    }

    @RequestMapping(value = "/user/project", method = RequestMethod.POST)
    public Iterable<Project> findUProject(@RequestBody @Validated({FormUser.ValidateUserEmail.class}) final FormUser form) {
        return userRepository.findProject(form.getEmail());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public Status createUser(@RequestBody @Valid final FormUser form,
                             @ModelAttribute("user") CurrentUser currentUser) throws AppException {
        if (repository.countByEmail(form.getEmail()) > 0) {
            LOG.info("User email already exists.");
            throw new AppException("user.email.exists");
        }
        if (repository.countByFnameAndLname(form.getFname(), form.getLname()) > 0) {
            LOG.info("User name already exists.");
            throw new AppException("user.name.exists");
        }
        if (StringUtils.isBlank(form.getPassword())) {
            form.setPassword(defPassword);
        }
        userManager.createUser(form);
        mailService.sendInviteEmail(form.getEmail(), StringUtil.prettyName(form), form.getPassword());
        if (form.getProjectId() != null) {
            UserProject userProject = new UserProject();
            userProject.setProject(projectRepository.findOne(Long.parseLong(form.getProjectId())));
            userProject.setUser(userRepository.findByEmail(form.getEmail()).orElse(null));
            userProjectRepository.save(userProject);
        }
        return new Status();
    }


    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    @Transactional
    public Status updateProfile(@RequestBody @Valid final FormUser form,
                                @ModelAttribute("user") CurrentUser currentUser) throws AppException {
        User user = repository.findUserByEmail(form.getEmail());
        if (StringUtils.isNotBlank(form.getFname())) user.setFname(form.getFname());
        user.setMname(form.getMname());
        if (StringUtils.isNotBlank(form.getLname())) user.setLname(form.getLname());
        user.setEnabled(form.getEnabled());
        if (StringUtils.isNotBlank(form.getRole())) user.setRole(form.getRole());
        user.setAccountNonLocked(form.getAccountNonLocked());
        return new Status();
    }

}
