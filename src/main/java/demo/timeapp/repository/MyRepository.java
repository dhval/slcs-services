package demo.timeapp.repository;

import demo.timeapp.entity.Audit;
import demo.timeapp.entity.Project;
import demo.timeapp.entity.Sheet;
import demo.timeapp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by dhval on 4/3/16.
 *
 * http://docs.spring.io/spring-data/jpa/docs/1.4.3.RELEASE/reference/html/jpa.repositories.html
 */
@org.springframework.stereotype.Repository
public interface MyRepository extends Repository<User, Long> {

    Iterable<User> findAll();

    Long countByEmail(String email);

    Long countByFnameAndLname(String fName, String lName);

    @Query("SELECT u from  User u where u.role='user'")
    public List<User> findAllUsers();

    @Query("SELECT u from  User u where fname =?1")
    public List<User> findByName(String fname);

    @Query("SELECT p from  Project p where name =?1")
    public Project findProjectByName(String name);

    @Query("SELECT u from  User u where email =?1")
    public User findUserByEmail(String name);

    @Query("SELECT s from  Sheet s where id = :id")
    public Sheet findSheetById(@Param("id") Long id);

    @Query("SELECT s from  Sheet s where s.user in ( select u.id from User u where u.email = :email)")
    public List<Sheet> findSheetByUser(@Param("email") String email);

    @Query("SELECT s from  Sheet s")
    public List<Sheet> findSheets();

    @Query("SELECT max(s.beginDt) from  Sheet s, User u where u.email = :email and s.user.id = u.id")
    public Date findLastSheetByUser(@Param("email") String email);

    @Query("select a from Audit a where a.time in (select max(a2.time) from Audit a2 group by a2.userId)")
    public List<Audit> findTopAudit();

}
