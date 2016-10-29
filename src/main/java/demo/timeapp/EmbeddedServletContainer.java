package demo.timeapp;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by dhval on 4/5/16.
 */
public class EmbeddedServletContainer  {}

/**extends  TomcatEmbeddedServletContainerFactory{
    @Value("${spring.datasource.url}")
    private String dsURL;

    @Value("${mysql.pass}")
    private String myPwd;

    @Override
    protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
            Tomcat tomcat) {
        tomcat.enableNaming();
        return super.getTomcatEmbeddedServletContainer(tomcat);
    }

    @Override
    protected void postProcessContext(Context context) {
        ContextResource resource = new ContextResource();
        resource.setName("jdbc/timesheet");
        resource.setType(DataSource.class.getName());
        resource.setProperty("username", "timesheet");
        resource.setProperty("password", myPwd);
        resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        resource.setProperty("url", dsURL);

        context.getNamingResources().addResource(resource);
    }

    @Bean(destroyMethod = "")
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/timesheet");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }


}**/
