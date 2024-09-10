package be.jimsa.reddoctor.config.document.annotation;


import be.jimsa.reddoctor.ws.model.dto.PatientDto;
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
                        responseCode = "200",
                        description = "Updating an appointment with the provided info was successful",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": true,
                                            "timestamp": "10/09/2024 10:27:38 PM",
                                            "result": {
                                                "date": "11-12-2024",
                                                "start": "11:30:10",
                                                "end": "12:00:10",
                                                "public_id": "i625bsvXoeAwTFiPGd6Huo4gGIXgCqkMtuF1rwPMTN_sUiDq4kuHgJWIljKc715O",
                                                "patient": {
                                                    "name": "Foo bar",
                                                    "phone_number": "9131231234"
                                                }
                                            }
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
                                            "timestamp": "10/09/2024 10:26:24 PM",
                                            "result": {
                                                "path": "POST /v0.9/patients/2Vpi103xK5OblG8ykrm8GOimac7W5ESDHxGbL_5dlwvBB6D6xxXeCTlcZgc-8cyB",
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
                description = "This request comes with a name and phone number",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = "A valid request with a valid body #1",
                                        summary = "Valid example #1",
                                        value = """
                                                {
                                                    "name": "Foo bar",
                                                    "phone_number": "9131231234"
                                                }
                                                """),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #1",
                                        summary = "Invalid example #1",
                                        value = """
                                                {
                                                    "phone_number": "9131231234"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #2",
                                        summary = "Invalid example #2",
                                        value = """
                                                {
                                                    "name": "Foo bar"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #3",
                                        summary = "Invalid example #3",
                                        value = """
                                                {
                                                    "name": "Foo",
                                                    "phone_number": "000"
                                                }
                                                """
                                )
                        },
                        schema = @Schema(implementation = PatientDto.class)
                )
        )
)
public @interface UpdateAnAppointmentRequestDocument {
    String summary() default "Default summary";

    String description() default "Default description";
}
