package dev.me.webquizengine.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotNullAndMinSizeConstraintValidator implements ConstraintValidator<NotNullAndMinSizeConstraint, List<String>> {

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.size() >= 2;
        }
        return false;

    }
}
