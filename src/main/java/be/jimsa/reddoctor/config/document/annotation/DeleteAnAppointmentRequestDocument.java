package be.jimsa.reddoctor.config.document.annotation;


import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
                        responseCode = API_DOCUMENT_RESPONSE_CODE_200,
                        description = API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_200_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_200_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_400,
                        description = API_DOCUMENT_400_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_400_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_404,
                        description = API_DOCUMENT_404_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_404_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_406,
                        description = API_DOCUMENT_406_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_406_EXAMPLE),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = API_DOCUMENT_RESPONSE_CODE_409,
                        description = API_DOCUMENT_409_DESCRIPTION,
                        content = @Content(
                                examples = @ExampleObject(API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_409_EXAMPLE),
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
        }
)
public @interface DeleteAnAppointmentRequestDocument {
    String summary() default API_DOCUMENT_DEFAULT_SUMMERY;
    String description() default API_DOCUMENT_DEFAULT_DESCRIPTION;
}
