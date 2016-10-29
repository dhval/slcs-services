package demo.timeapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Created by dhval on 1/24/16.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter implements EnvironmentAware {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("file:dist/");
                //.resourceChain(true).addResolver(new GzipResourceResolver());
        registry.addResourceHandler("/views/**")
                .addResourceLocations("file:views/");
        registry.addResourceHandler("/template/**")
                .addResourceLocations("file:template/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:css/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:img/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:js/");
    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
