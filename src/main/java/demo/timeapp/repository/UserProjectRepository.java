package demo.timeapp.repository;

import demo.timeapp.entity.Project;
import demo.timeapp.entity.User;
import demo.timeapp.entity.UserProject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by dhval on 7/30/16.
 */
public interface UserProjectRepository extends CrudRepository<UserProject, Long> {
    UserProject save(UserProject entity);
    void delete(Long id);
    Long deleteByUser(User user);
    UserProject findOne(Long aLong);
    boolean exists(Long id);
    Iterable<UserProject> findAll();
    Iterable<UserProject> findByProject(Project project);
    Optional<UserProject> findByUser(User user);
    @Modifying
    @Transactional
    @Query(value = "delete from user_project u where u.person_id = :id", nativeQuery = true)
    void deleteUsers(@Param("id") Long id);
}
