package demo.timeapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dhval on 4/18/16.
 */
public class CSRFExceptionHandler extends AccessDeniedHandlerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CSRFExceptionHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOG.warn(accessDeniedException.getMessage(), accessDeniedException.getStackTrace());
        if(accessDeniedException instanceof MissingCsrfTokenException
                || accessDeniedException instanceof InvalidCsrfTokenException) {
            throw new ServletException(accessDeniedException);
        }
        super.handle(request, response, accessDeniedException);
    }
}
