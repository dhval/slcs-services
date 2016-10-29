package demo.timeapp.tasks;

import demo.timeapp.entity.Metrics;
import demo.timeapp.repository.MetricsRepository;
import demo.timeapp.util.CommonUtil;
import demo.timeapp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhval on 4/27/16.
 */
@Component
public class Health {
    private static final Logger LOG = LoggerFactory.getLogger(Health.class);


    @Autowired @Lazy
    private MetricsRepository repository;

    @Autowired
    private SystemPublicMetrics metrics;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // http://stackoverflow.com/questions/26147044/spring-cron-expression-for-every-day-101am
    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void reportHealthMetrics() {
        LOG.info("The time is now " + dateFormat.format(new Date()));
        Collection<Metric<?>> myMetrics = metrics.metrics();
        Map<String, Number> map = new HashMap<>();
        for(Metric m : myMetrics) {
            map.put(m.getName(), m.getValue());
        }
        Metrics metrics = new Metrics("system.status:" + CommonUtil.hostName());
        metrics.setContent(StringUtil.toJSON(map));
        repository.save(metrics);
    }
}
