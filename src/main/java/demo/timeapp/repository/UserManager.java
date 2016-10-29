package demo.timeapp.repository;

import demo.timeapp.AppException;
import demo.timeapp.dto.FormUser;
import demo.timeapp.entity.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by dhval on 9/21/16.
 */

@Service
public class UserManager {
    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);

    @Autowired
    private UserRepository userRepository;

    public void createUser(FormUser form) throws AppException {
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            LOG.info("User email already exists.");
            throw new AppException("user.email.exists");
        }
        if (userRepository.findByLnameAndFname(form.getLname(), form.getFname()).isPresent()) {
            LOG.info("User name already exists.");
            throw new AppException("user.name.exists");
        }

        User user = new User();
        user.setFname(form.getFname());
        user.setMname(form.getMname());
        user.setLname(form.getLname());
        user.setEmail(form.getEmail());
        // enable user
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        if (StringUtils.isNotBlank(form.getRole()) &&
                form.getRole().equals("admin"))
            user.setRole("admin");
        else
            user.setRole("user");
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw new AppException("error.database");
        }
    }
}
