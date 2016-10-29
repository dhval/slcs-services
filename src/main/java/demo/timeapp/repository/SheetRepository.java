package demo.timeapp.repository;

import demo.timeapp.entity.Sheet;
import demo.timeapp.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by dhval on 4/17/16.
 */
@org.springframework.stereotype.Repository
public interface SheetRepository extends Repository<Sheet, Long>  {

    // Pending approval for the week
    Long countByStatusAndBeginDt(String status, Date beginDt);

    void delete(Long id);
     //ByOrderByBeginDesc
    Sheet findTopByUserAndStatusOrderByBeginDtAsc(User user, String status);

    Sheet findTopByUserAndBeginDt(User user, Date begin);

    List<Sheet> findByUserOrderByBeginDtDesc(User user);

    List<Sheet> findByUserAndStatusAndBeginDtBetweenOrderByBeginDtDesc(User user, String status, Date begin, Date end);

    List<Sheet> findByUserAndBeginDtBetweenOrderByBeginDtDesc(User user, Date begin, Date end);

    Sheet findTopByOrderByBeginDtDesc();

    Slice<Sheet> findTop3ByOrderByBeginDtDesc(String lastname, Pageable pageable);

    List<Sheet> findFirst10ByOrderByBeginDtDesc(String lastname, Sort sort);

    List<Sheet> findTop10ByOrderByBeginDtDesc(String lastname, Pageable pageable);
}
