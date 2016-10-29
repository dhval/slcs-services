package demo.timeapp.controller;

import demo.timeapp.AppException;
import demo.timeapp.dto.CurrentUser;
import demo.timeapp.service.MessageBundle;
import demo.timeapp.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dhval on 1/27/16.
 */
@ControllerAdvice(basePackages = {"demo.timeapp"})
@SessionAttributes("user")
public class ControllerSupport {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerSupport.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    MessageBundle bundle;

 //   @Autowired
 //   ProjectValidator projectValidator;

    @ModelAttribute("user")
    public CurrentUser user(@AuthenticationPrincipal CurrentUser user) {
        return user;
    }

    // Add response headers to resources served from Spring Dispatcher
    @ModelAttribute
    public void addResponseHeader(HttpServletResponse response) {
        response.setHeader("X-FRAME-OPTIONS", "DENY");
        response.setHeader("X-UA-Compatible", "IE=edge");
        response.setHeader("Request-Source", "APPLICATION");
        // Gracefully ask not to cache JSON or templates.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView("/index");
        mav.addObject("exception", e);
        return mav;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleViolationException(ValidationException exception) {
        LOG.info("ddd");
        Map<String, String> errors = exception.getErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fe -> bundle.getMessage(fe.getDefaultMessage())));
        return Collections.singletonMap("validate", errors);
    }

    @ExceptionHandler(AppException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleException(AppException exception) {
        LOG.info(exception.getContent());
        LOG.warn(exception.getMessage(), exception.getStackTrace());
        return newDefaultErrorMessage(exception.getContent());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleException(MethodArgumentNotValidException exception) {
        LOG.warn(exception.getMessage(), exception.getStackTrace());
        Map<String, Object> message = new HashMap<>();
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.put(error.getField(), bundle.getMessage(error.getDefaultMessage()));
            LOG.warn(error.getCode());
            LOG.warn(error.getDefaultMessage());
        }
        return Collections.singletonMap("validate", message);
    }

    /**

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleCException(Exception exception) {
        LOG.info("dd3");
        LOG.warn(exception.getMessage(), exception.getStackTrace());
        LOG.warn(exception.getMessage(), exception.getCause());
        return Collections.singletonMap("error", newDefaultErrorMessage(""));
    }
**/
    private Map<String, Object> newDefaultErrorMessage(String msg) {
        if (StringUtils.isEmpty(msg))
            msg = MessageBundle.DEF_ERROR_KEY;
        Map<String, Object> message = new HashMap<>();
        message.put("title", "Error");
        message.put("content", bundle.getMessage(msg));
        return message;
    }

}
