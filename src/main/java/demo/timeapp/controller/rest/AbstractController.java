package demo.timeapp.controller.rest;

import demo.timeapp.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

/**
 * Created by dhval on 7/28/16.
 */
@RequestMapping("/public")
public abstract class AbstractController {
    static ResponseEntity<Map<String, String>> msg(Integer code, String describe) {
        return  new ResponseEntity<>(Collections.singletonMap(Integer.toString(code), describe) , HttpStatus.OK);
    }

    void validate(Object obj, Validator validator, BindingResult result) throws ValidationException {
        validator.validate(obj ,result);
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }

}
