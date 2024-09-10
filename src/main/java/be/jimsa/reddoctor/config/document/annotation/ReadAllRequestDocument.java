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
                        description = "Reading all appointments was successful",
                        content = @Content(
                                examples = {
                                        @ExampleObject(
                                                name = "when there are three items",
                                                summary = "Response with values",
                                                value = """
                                                        {
                                                            "action": true,
                                                            "timestamp": "10/09/2024 10:20:27 PM",
                                                            "result": [
                                                                {
                                                                    "date": "11-12-2024",
                                                                    "start": "11:30:10",
                                                                    "end": "12:00:10",
                                                                    "public_id": "6ZCGPsreW4NoaRj12dhZBFRMH1jjzDOdEVHYDsivUdr5r6_JLQ5Mx69VCFHb7aQ4",
                                                                    "patient": null
                                                                },
                                                                {
                                                                    "date": "11-12-2024",
                                                                    "start": "12:00:10",
                                                                    "end": "12:30:10",
                                                                    "public_id": "f6we3b2vdp-VQtjmrSIbyEWdO3Klj02JgCGuQHHIB27Yk1wxv2layTeBrVT_YNry",
                                                                    "patient": null
                                                                }
                                                            ]
                                                        }
                                                        """),
                                        @ExampleObject(
                                                name = "when there is no item!",
                                                summary = "Response without value",
                                                value = """
                                                        {
                                                          "action": true,
                                                          "timestamp": "2024-09-02T13:54:39.976049836",
                                                          "result": []
                                                        }
                                                        """
                                        )

                                },
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
                                                "path": "GET /{version}/doctors/11-12-2024?page=1&size=15&sort_direction=asc&type=all",
                                                "message": "Internal service error!"
                                            }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                )
        }
)
public @interface ReadAllRequestDocument {
    String summary() default "Default summary";

    String description() default "Default description";
}
