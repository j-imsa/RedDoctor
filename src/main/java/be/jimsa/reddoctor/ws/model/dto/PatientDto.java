package be.jimsa.reddoctor.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Data
@Builder
@Schema(name = PATIENT_DTO_DOCUMENT_NAME, description = PATIENT_DTO_DOCUMENT_DESCRIPTION)
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    @NotEmpty(message = PATIENT_VALIDATION_NAME_NOT_EMPTY_MESSAGE)
    @NotBlank(message = PATIENT_VALIDATION_NAME_NOT_BLANK_MESSAGE)
    @Size(min = PATIENT_VALIDATION_MIN_LENGTH, max = PATIENT_VALIDATION_MAX_LENGTH, message = PATIENT_VALIDATION_LENGTH_MESSAGE)
    @Schema(description = PATIENT_DOCUMENT_NAME_PATTERN_DESCRIPTION, example = PATIENT_DOCUMENT_NAME_FIELD_EXAMPLE)
    private String name;

    @JsonProperty(PATIENT_PHONE_NUMBER_FIELD)
    @Pattern(regexp = PATIENT_VALIDATION_PHONE_NUMBER_PATTERN, message = PATIENT_VALIDATION_PHONE_NUMBER_PATTERN_MESSAGE)
    @NotEmpty(message = PATIENT_VALIDATION_PHONE_NUMBER_NOT_EMPTY_MESSAGE)
    @NotBlank(message = PATIENT_VALIDATION_PHONE_NUMBER_NOT_BLANK_MESSAGE)
    @Schema(description = PATIENT_DOCUMENT_PHONE_NUMBER_PATTERN_DESCRIPTION, example = PATIENT_DOCUMENT_PHONE_NUMBER_FIELD_EXAMPLE)
    private String phoneNumber;
}
