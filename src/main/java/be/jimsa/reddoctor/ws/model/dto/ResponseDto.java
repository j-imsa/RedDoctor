package be.jimsa.reddoctor.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@Data
@Builder
@Schema(name = RESPONSE_DOCUMENT_NAME, description = RESPONSE_DOCUMENT_DESCRIPTION)
public class ResponseDto {

    @Schema(description = RESPONSE_DOCUMENT_ACTION_DESCRIPTION, example = RESPONSE_DOCUMENT_ACTION_EXAMPLE)
    private boolean action;

    @Schema(description = RESPONSE_DOCUMENT_TIMESTAMP, example = RESPONSE_DATE_TIME_FORMAT_EXAMPLE)
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;

    @Schema(description = RESPONSE_DOCUMENT_RESULT)
    private Object result;
}
