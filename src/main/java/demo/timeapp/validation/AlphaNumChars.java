package demo.timeapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * Created by dhval on 5/12/16.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[0-9a-zA-Z _-]+$")
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface AlphaNumChars {

    String message() default "{invalid.alpha.chars}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AlphaNumChars[] value();
    }
}
