package be.jimsa.reddoctor.config.validation.annotation;

import be.jimsa.reddoctor.config.validation.TimeSequenceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.GENERAL_VALIDATION_TIME_SEQUENCE_DEFAULT_MESSAGE;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeSequenceValidator.class)
public @interface ValidTimeSequence {
    String message() default GENERAL_VALIDATION_TIME_SEQUENCE_DEFAULT_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String currentTime();

    String nextTime();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidTimeSequence[] value();
    }
}
