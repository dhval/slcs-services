package demo.timeapp.repository;

import demo.timeapp.entity.Project;
import demo.timeapp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by dhval on 7/26/16.
 */
@org.springframework.stereotype.Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @Query("FROM Project p WHERE p.name = 'Default' and p.client = 'SLCS LLC'")
    Project def();
    @Query("select u from Project p, User u, UserProject up where p.name=:name and p.client=:client and p.id = up.project.id and u.id = up.user.id")
    public Iterable<User> findUser(@Param("name") String name, @Param("client") String client);
    Project save(Project project);
    void delete(Long id);
    Optional<Project> findById(Long id);
    Optional<Project> findTopByName(String name);
    Optional<Project> findTopByNameAndClient(String name, String client);
    Iterable<Project> findAll();

}
