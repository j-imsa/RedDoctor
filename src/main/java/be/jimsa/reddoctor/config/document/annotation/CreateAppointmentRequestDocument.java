package be.jimsa.reddoctor.config.document.annotation;


import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_201,
                        description = API_DOCUMENT_APPOINTMENT_POST_201_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_POST_201_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_400,
                        description = API_DOCUMENT_400_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_POST_400_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_500,
                        description = API_DOCUMENT_500_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_500_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                )
        },
        requestBody = @RequestBody(
                description = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_DESCRIPTION,
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_NAME,
                                        summary = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_SUMMERY,
                                        value = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_VALUE
                                ),
                                @ExampleObject(
                                        name = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_NAME,
                                        summary = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_SUMMERY,
                                        value = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_VALUE
                                ),
                                @ExampleObject(
                                        name = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_NAME,
                                        summary = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_SUMMERY,
                                        value = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_VALUE
                                ),
                                @ExampleObject(
                                        name = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_NAME,
                                        summary = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_SUMMERY,
                                        value = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_VALUE
                                ),
                                @ExampleObject(
                                        name = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_NAME,
                                        summary = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_SUMMERY,
                                        value = API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_VALUE
                                ),
                        },
                        schema = @Schema(implementation = AppointmentDto.class)
                )
        )
)
public @interface CreateAppointmentRequestDocument {
    String summary() default API_DOCUMENT_DEFAULT_SUMMERY;
    String description() default API_DOCUMENT_DEFAULT_DESCRIPTION;
}
