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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Deleting an appointment with the provided public_id was successful",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": true,
                                            "timestamp": "10/09/2024 10:18:23 PM",
                                            "result": true
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Due to invalid public_id, it responded a not-found",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": false,
                                            "timestamp": "10/09/2024 10:17:26 PM",
                                            "result": {
                                                "path": "DELETE /{version}/doctors/nFy_PXVUyw2QA8PIQBBtGpVo5GnSsLgXUab-nuVCJyEuPpgCpLa_psO6BFcZLnBX",
                                                "message": "The resource with provided public_id not founded!"
                                            }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error has occurred",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": false,
                                            "timestamp": "2024-09-02T02:21:41.009081702",
                                            "result": {
                                                "path": "DELETE /{version}/doctors",
                                                "message": "Internal service error!"
                                            }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                )
        }
)
public @interface DeleteAnAppointmentRequestDocument {
    String summary() default "Default summary";

    String description() default "Default description";
}
