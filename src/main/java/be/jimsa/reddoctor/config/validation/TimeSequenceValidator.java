package be.jimsa.reddoctor.config.validation;

import be.jimsa.reddoctor.config.validation.annotation.ValidTimeSequence;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalTime;
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
        LocalTime time1 = (LocalTime) new BeanWrapperImpl(value).getPropertyValue(currentTime);
        LocalTime time2 = (LocalTime) new BeanWrapperImpl(value).getPropertyValue(nextTime);
        return !Objects.requireNonNull(time2).isBefore(time1) && !time2.equals(time1);
    }
}
