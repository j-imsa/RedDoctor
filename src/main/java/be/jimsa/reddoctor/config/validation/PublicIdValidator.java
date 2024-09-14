package be.jimsa.reddoctor.config.validation;


import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


public class PublicIdValidator implements ConstraintValidator<ValidPublicId, String> {

    @Override
    public void initialize(ValidPublicId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String publicId, ConstraintValidatorContext context) {
        if (publicId == null) {
            return false;
        }
        return publicId.matches(PUBLIC_ID_PATTERN) &&
                publicId.length() >= PUBLIC_ID_MIN_LENGTH &&
                publicId.length() <= PUBLIC_ID_MAX_LENGTH;
    }


}
