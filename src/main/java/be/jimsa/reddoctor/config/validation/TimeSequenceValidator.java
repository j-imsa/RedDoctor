package be.jimsa.reddoctor.config.validation;

import be.jimsa.reddoctor.config.validation.annotation.ValidTimeSequence;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TimeSequenceValidator implements ConstraintValidator<ValidTimeSequence, Object> {

    private String currentTime;
    private String nextTime;

    @Override
    public void initialize(ValidTimeSequence constraintAnnotation) {
        this.currentTime = constraintAnnotation.currentTime();
        this.nextTime = constraintAnnotation.nextTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalTime start = Objects.requireNonNull((LocalTime) new BeanWrapperImpl(value).getPropertyValue(currentTime));
        LocalTime end = Objects.requireNonNull((LocalTime) new BeanWrapperImpl(value).getPropertyValue(nextTime));

        if (end.equals(LocalTime.MIDNIGHT.truncatedTo(ChronoUnit.SECONDS))) { // end cannot be 00:00:00
            return false;
        }
        if (start.equals(LocalTime.MAX.truncatedTo(ChronoUnit.SECONDS))) { // start cannot be 23:59:59
            return false;
        }
        if (start.equals(end)) { // abs(end-start) == 0 does not make sense!
            return false;
        }

        // return Duration.between(start, end).compareTo(Duration.ofSeconds(1)) > 0;

        // if start > end then -1
        Duration duration = Duration.between(start, end);
        if (duration.isNegative()) {
            return false;
        } else return !duration.isZero();
    }
}
