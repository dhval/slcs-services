package demo.timeapp.audit;

import demo.timeapp.entity.Audit;
import demo.timeapp.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhval on 4/26/16.
 */
@Aspect
@Component
public class Metrics {
    private static final Logger LOG = LoggerFactory.getLogger(Metrics.class);

    private final CounterService counterService;

    @Autowired
    public Metrics(CounterService counterService) {
        this.counterService = counterService;
    }

    /**

    @Autowired
    private BufferMetricReader metrics;

    int count = metrics.findOne("my.metrics.key").getValue().intValue();

     */

    @Autowired @Lazy
    private AuditRepository repository;

    @Transactional
    @Before("execution(* demo.timeapp.controller.Login.forgotReset(..))")
    public void forgotReset(JoinPoint thisJoinPoint) {
        Object[] signatureArgs = thisJoinPoint.getArgs();
        Audit audit = new Audit( (String) signatureArgs[0], "audit.password.reset");
        repository.save(audit);
    }

    @AfterReturning(pointcut = "execution(* demo.timeapp.controller.Login.login(..))", returning = "retval")
    public void afterGreeting(Object retval) {
        LOG.info(retval.toString());
    }

    @Bean
    public ApplicationListener success() {
        return new ApplicationListener<AuthenticationSuccessEvent>() {
            @Override
            @Transactional
            public void onApplicationEvent(AuthenticationSuccessEvent event) {
                UserDetails ud = (UserDetails) event.getAuthentication().getPrincipal();
                Audit audit = new Audit(ud.getUsername(), "audit.login.success");
                repository.save(audit);
                LOG.info(ud.getUsername() + " logged in successfully");
                counterService.increment("counter.user_login_success");
            }
        };
    }

    @Bean
    public ApplicationListener badCredentials() {
        return new ApplicationListener<AuthenticationFailureBadCredentialsEvent>() {
            @Override
            @Transactional
            public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
                Audit audit = new Audit(event.getAuthentication().getPrincipal().toString(), "audit.login.bad.credentials");
                repository.save(audit);
            }
        };
    }

    @Bean
    public ApplicationListener authFailure() {
        return new ApplicationListener<AbstractAuthenticationFailureEvent>() {
            @Override
            @Transactional
            public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
                LOG.info(event.toString() + " login failed");
                Audit audit = new Audit("auth.failure", "auth.failure");
                audit.setContent(event.toString());
                repository.save(audit);
                counterService.increment("counter.user_login_fail");
            }
        };
    }



}
