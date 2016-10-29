package demo.timeapp.service;

/**
 * Created by dhval on 1/24/16.
 */

import demo.timeapp.entity.User;
import org.springframework.data.repository.Repository;

public interface UserService extends Repository<User, Long> {

    User getUserById(long id);

    User getUserByEmail(String email);

    Iterable<User> findAll();

    User save(User form);

}