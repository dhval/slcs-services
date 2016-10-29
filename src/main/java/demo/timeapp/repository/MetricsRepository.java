package demo.timeapp.repository;


import demo.timeapp.entity.Metrics;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by dhval on 9/21/16.
 */
public interface MetricsRepository extends CrudRepository<Metrics, Long>, JpaSpecificationExecutor<Metrics> {
    List<Metrics> findAll(Specification<Metrics> spec);
    Metrics save(Metrics audit);
}
