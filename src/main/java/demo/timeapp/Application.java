package demo.timeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import javax.validation.Validator;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync
@PropertySource(value = {"application.properties", "config.properties"})
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private
    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        // http://stackoverflow.com/questions/28821521/configure-datasource-programmatically-in-spring-boot
        return DataSourceBuilder.create().driverClassName("org.postgresql.Driver").build();
    }

    @Bean
    public SpringBootServletInitializer servletInitializer() {
        return new SpringBootServletInitializer() {
            @Override
            protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
                return application.sources(Application.class);
            }
        };
    }

/**
 @Bean public Validator validator () {

 beanFactory.autowireBean(new ProjectValidator());
 ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
 .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory))
 .buildValidatorFactory();
 Validator validator = validatorFactory.getValidator();


 return validator;
 }

 **/

/**
 @Bean public Validator validator() {
 return new LocalValidatorFactoryBean().getValidator();
 }

 @Bean public MethodValidationPostProcessor mvpp() {
 MethodValidationPostProcessor mvpp = new MethodValidationPostProcessor();
 mvpp.setValidator(validator());

 return mvpp;
 }
 **/

    @Bean
    public Validator validator() {
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
