package demo.timeapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by dhval on 7/29/16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.TYPE,ElementType.PARAMETER})
@Constraint(validatedBy = {ProjectValidator.class})
public @interface ProjectValid {

    String message() default "{project.invalid}";
    Class<?>[] groups() default  {};
    Class<? extends Payload>[] payload() default {};
}
