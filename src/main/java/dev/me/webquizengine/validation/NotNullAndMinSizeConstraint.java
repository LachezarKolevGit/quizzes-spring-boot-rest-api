package dev.me.webquizengine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target( {FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullAndMinSizeConstraintValidator.class)
@Documented
public @interface NotNullAndMinSizeConstraint {

String message() default "{Options can't be less than 2}";

Class<?>[] groups() default { };

Class<? extends Payload>[] payload() default { };

}
