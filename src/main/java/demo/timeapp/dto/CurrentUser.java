package demo.timeapp.dto;

import demo.timeapp.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by dhval on 1/24/16.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        // we are using email instead of userId as this is we want as an identifier.
        super(user.getEmail(), user.getPasswordHash(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), AuthorityUtils.createAuthorityList(user.getRole()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole().equals("admin") ? Role.ADMIN  : Role.USER;
    }
}
