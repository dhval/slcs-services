package demo.timeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by dhval on 4/25/16.
 */
@Component
public class MessageBundle {
    public static final String DEF_ERROR_KEY = "error.msg";

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, key, null);
    }

    @PostConstruct
    public void init() {
    }

}
