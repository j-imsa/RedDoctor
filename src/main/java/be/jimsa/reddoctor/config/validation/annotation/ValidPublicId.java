package be.jimsa.reddoctor.config.validation.annotation;

import be.jimsa.reddoctor.config.validation.PublicIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PublicIdValidator.class)
public @interface ValidPublicId {
    String message() default "Invalid public_id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
