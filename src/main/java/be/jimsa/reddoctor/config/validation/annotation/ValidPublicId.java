package be.jimsa.reddoctor.config.validation.annotation;

import be.jimsa.reddoctor.config.validation.PublicIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.GENERAL_VALIDATION_PUBLIC_ID_DEFAULT_MESSAGE;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PublicIdValidator.class)
public @interface ValidPublicId {
    String message() default GENERAL_VALIDATION_PUBLIC_ID_DEFAULT_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
