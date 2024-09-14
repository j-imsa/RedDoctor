package be.jimsa.reddoctor.config.document.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        description = "The public_id of the resource",
        example = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e",
        examples = {
                @ExampleObject(name = "Valid example", value = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e", summary = "Example public ID 1, Valid"),
                @ExampleObject(name = "Invalid example", value = "xyz789xyz789xyz789x", summary = "Example public ID 2, Invalid")
        }
)
public @interface PublicIdParameterDocument {
}
