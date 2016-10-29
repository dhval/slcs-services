package demo.timeapp.repository;

import demo.timeapp.entity.Audit;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by dhval on 4/28/16.
 */
public class AuditSpec implements Specification<Audit> {

    private final Audit audit;

    public AuditSpec(Audit audit) {
        this.audit = audit;
    }

    @Override
    public Predicate toPredicate(Root<Audit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return null;
    }
}
