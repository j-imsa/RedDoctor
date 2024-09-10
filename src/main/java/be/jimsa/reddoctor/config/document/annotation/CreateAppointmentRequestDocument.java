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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Creating appointments with the provided info was successful",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": true,
                                            "timestamp": "10/09/2024 10:20:30 PM",
                                            "result": [
                                                {
                                                    "date": "11-12-2024",
                                                    "start": "11:30:10",
                                                    "end": "12:00:10",
                                                    "public_id": "RIXzFN_gwWmX2qXtY_fMT3HsLZ8fB4Roypdwzf1cQCglaBu8yznm0VXXqdzc01BI",
                                                    "patient": null
                                                },
                                                {
                                                    "date": "11-12-2024",
                                                    "start": "12:00:10",
                                                    "end": "12:30:10",
                                                    "public_id": "1_ufS5cjPkmwmcTYIC1rTSvHdJIHP3IfDHY_N-2FXER1J3Vv4fLOE3Z8VYkfl3lG",
                                                    "patient": null
                                                }
                                            ]
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Due to invalid inputs, it responded as a bad request",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": false,
                                            "timestamp": "10/09/2024 10:01:13 PM",
                                            "result": {
                                                "publicId": "public_id must be null on the creation operation"
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
                                            "timestamp": "10/09/2024 10:20:30 PM",
                                            "result": {
                                                "path": "POST /{version}/doctors",
                                                "message": "Internal service error!"
                                            }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                )
        },
        requestBody = @RequestBody(
                description = "This request comes with a date, start and end time",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = "A valid request with a valid body #1",
                                        summary = "Valid example #1",
                                        value = """
                                                {
                                                    "date": "11-12-2024",
                                                    "start": "11:30:10",
                                                    "end": "12:35:00"
                                                }
                                                """),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #1",
                                        summary = "Invalid example #1",
                                        value = """
                                                {
                                                    "start": "11:30:10",
                                                    "end": "12:35:00"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #2",
                                        summary = "Invalid example #2",
                                        value = """
                                                {
                                                    "date": "11-12-2024",
                                                    "start": "11:30:10"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #3",
                                        summary = "Invalid example #3",
                                        value = """
                                                {
                                                     "date": "11-12-2024",
                                                     "end": "12:35:00"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #4",
                                        summary = "Invalid example #4",
                                        value = """
                                                {
                                                    "date": "11-12-2024"
                                                }
                                                """
                                ),
                        },
                        schema = @Schema(implementation = AppointmentDto.class)
                )
        )
)
public @interface CreateAppointmentRequestDocument {
    String summary() default "Default summary";

    String description() default "Default description";
}
