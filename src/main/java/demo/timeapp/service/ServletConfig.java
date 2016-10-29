package demo.timeapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

/**
 * Created by dhval on 4/5/16.
 */
@Component
public class ServletConfig implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        /**
         * Good to know but we will do using application.properties.
         * http://www.baeldung.com/spring-boot-application-configuration
         */

      //  container.setPort(8000);
      //  container.setContextPath("/services");
    }
}

