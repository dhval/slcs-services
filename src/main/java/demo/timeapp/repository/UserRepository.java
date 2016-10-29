package demo.timeapp.repository;

import demo.timeapp.entity.Project;
import demo.timeapp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by dhval on 1/23/16.
 */

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
    @Query("from User u where u.role = 'user' and u.isEnabled='true'")
    List<User> findAllActiveUsers();
    User findOne(Long id);
    User save(User entity);
    Optional<User> findByEmail(String email);
    Optional<User> findByLnameAndFname(String lname, String fname);
    @Query("select p from Project p, User u, UserProject up where u.email=:email and u.id = up.user.id and up.project.id = p.id")
    public Iterable<Project> findProject(@Param("email") String email);
}
