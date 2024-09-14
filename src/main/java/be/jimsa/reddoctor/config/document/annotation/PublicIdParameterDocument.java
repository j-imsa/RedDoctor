package be.jimsa.reddoctor.config.document.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        description = API_DOCUMENT_PUBLIC_ID_DESCRIPTION,
        example = API_DOCUMENT_PUBLIC_ID_EXAMPLE,
        examples = {
                @ExampleObject(
                        name = API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_NAME,
                        value = API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_VALUE,
                        summary = API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_SUMMERY
                ),
                @ExampleObject(
                        name = API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_NAME,
                        value = API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_VALUE,
                        summary = API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_SUMMERY
                )
        }
)
public @interface PublicIdParameterDocument {
}
