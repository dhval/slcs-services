package demo.timeapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by dhval on 7/27/16.
 */
@XmlRootElement
public class ValidationException  extends Exception implements Serializable {

    private static final long serialVersionUID = 981122838745193943L;
    private static final Logger LOG = LoggerFactory.getLogger(ValidationException.class);
    private List<FieldError> errors;

    public ValidationException(BindingResult result) {
        LOG.info(result.toString());
        errors = result.getFieldErrors();
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
