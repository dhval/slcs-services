package demo.timeapp.repository;

import demo.timeapp.entity.Audit;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by dhval on 4/28/16.
 */
public interface AuditRepository extends CrudRepository<Audit, Long>, JpaSpecificationExecutor<Audit> {
    List<Audit> findAll(Specification<Audit> spec);
    List<Audit> findByTimeGreaterThan(Date date);
    Audit save(Audit audit);

    /**
     * We need a native query here as JPQL do not support limit clause.
     * http://stackoverflow.com/questions/6708085/select-top-1-result-using-jpa
     * @return
     */
    @Query(value = "select * from Audit a where a.time in (select max(a2.time) from Audit a2 group by a2.person_id) limit 15", nativeQuery = true)
    public List<Audit> findTopAudit();
}
