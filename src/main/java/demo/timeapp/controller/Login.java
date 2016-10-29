package demo.timeapp.controller;

import demo.timeapp.GmailService;
import demo.timeapp.dto.CurrentUser;
import demo.timeapp.dto.Role;
import demo.timeapp.entity.User;
import demo.timeapp.mail.MailService;
import demo.timeapp.repository.EntityRepository;
import demo.timeapp.repository.UserRepository;
import demo.timeapp.service.UserService;
import demo.timeapp.util.RandomUtil;
import demo.timeapp.mail.Mail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.internet.MimeMessage;

/**
 * Created by dhval on 1/24/16.
 */
@Controller
public class Login {
    private static final Logger LOG = LoggerFactory.getLogger(Login.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GmailService gmailService;

    @Autowired
    private EntityRepository repository;

    @Autowired
    private CounterService counterService;

    @RequestMapping("/")
    String home(@ModelAttribute("user") CurrentUser currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        return (currentUser.getRole().equals(Role.USER))? "user": "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        LOG.info(error);
        if (StringUtils.isNotBlank(error) && error.equals("unauthorized"))
            model.addAttribute("error", "login.unauthorized");
        return "login";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact() throws  Exception {
        return "contact";
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public String forgotInit() throws  Exception {
        return "forgot";
    }

    @Autowired
    private MailService mailService;

    @Transactional
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String forgotToken(@RequestParam("email") String email) throws  Exception {
        LOG.info(email);
        User user = userService.getUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        String token  = RandomUtil.generateToken();
        MimeMessage message = Mail.createHTMLEmail(email, "Password Reset", mailService.createTokenMail(token));
        Mail.sendMessage(gmailService.getGmailService(), message);
        user.setToken(Integer.parseInt(token));
        userRepository.save(user);
        return "reset";
    }


    @Transactional
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String forgotReset(@RequestParam("email") String email,
                              @RequestParam("token") String token,
                              @RequestParam("password") String password, Model model) {
        LOG.info(email);
        User user = userService.getUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        if (!Integer.toString(user.getToken()).equals(token)) {
            LOG.info("BAD Token - " + email + user.toString() + password);
            LOG.info("BAD Token - " + user.getToken() + " != " + token);
            model.addAttribute("error", "login.badtoken");
            return "reset";
        }
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);
        return "login";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }


}
