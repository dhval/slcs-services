package demo.timeapp.service;

import demo.timeapp.dto.CurrentUser;
import demo.timeapp.entity.Audit;
import demo.timeapp.entity.User;
import demo.timeapp.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhval on 1/24/16.
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    private final UserService userService;

    @Autowired
    private EntityRepository repository;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        } else {
            Audit audit = new Audit(email, "audit.login.attempt");
            repository.create(audit);
        }
        return new CurrentUser(user);
    }

}