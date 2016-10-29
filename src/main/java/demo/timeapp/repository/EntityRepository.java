package demo.timeapp.repository;

import demo.timeapp.entity.Audit;
import demo.timeapp.entity.Entry;
import demo.timeapp.entity.Metrics;
import demo.timeapp.entity.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by dhval on 4/2/16.
 */
@Repository
public class EntityRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepository.class);

    @PersistenceContext
    private EntityManager em;

    public Sheet create(Sheet sheet) {
        if (sheet.getId() != null && sheet.getId() > 0) {
            em.merge(sheet);
        } else {
            sheet.setStatus("entered");
            em.persist(sheet);

        }
        return sheet;
    }

    /**
    public void create(TimeEntry timeEntry) {
        if (timeEntry.getId() != null && timeEntry.getId() > 0) {
            em.merge(timeEntry);
        } else {
            List<Entry> entries = new ArrayList<>();
            for (Day day : Day.values()) {
                if (day == Day.NONE) continue;
                Entry entry = new Entry(day);
                // entry.setSheet(sheet);
                entries.add(entry);
                create(entry);
            }
            timeEntry.setEntries(entries);
            em.persist(timeEntry);
        }
    }
 **/
    public void create(Entry entry) {
        em.persist(entry);
    }

    public void create(Audit audit) {
        em.persist(audit);
    }

    public void create(Metrics metrics) {
        em.persist(metrics);
    }

    public void remove(Sheet entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

}
