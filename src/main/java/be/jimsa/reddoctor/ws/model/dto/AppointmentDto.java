package be.jimsa.reddoctor.ws.model.dto;

import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@Data
@Builder
@Schema(name = APPOINTMENT_DTO_DOCUMENT_NAME, description = APPOINTMENT_DTO_DOCUMENT_DESCRIPTION)
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    @JsonProperty(GENERAL_PUBLIC_ID_FIELD)
    @Null(message = APPOINTMENT_VALIDATION_PUBLIC_ID_NULL_MESSAGE, groups = {Create.class})
    @ValidPublicId(groups = Read.class)
    @Schema(
            type = "string",
            description = "The public_id of the resource",
            example = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e"
    )
    private String publicId;

    @JsonFormat(pattern = DATE_FORMAT)
    @NotNull(message = GENERAL_VALIDATION_DATE_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = "string", example = "12-30-45", description = DATE_FORMAT)
    private LocalDate date;

    @JsonFormat(pattern = TIME_FORMAT)
    @NotNull(message = APPOINTMENT_VALIDATION_START_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = "string", example = "12:30:45", description = TIME_FORMAT)
    private LocalTime start;

    @JsonFormat(pattern = TIME_FORMAT)
    @NotNull(message = APPOINTMENT_VALIDATION_END_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = "string", example = "18:30:45", description = TIME_FORMAT)
    private LocalTime end;

    @Null(message = APPOINTMENT_VALIDATION_TYPE_NULL_MESSAGE, groups = {Create.class})
    @JsonIgnore
    private String type;

    @JsonProperty(PATIENT_FIELD)
    @Null(message = APPOINTMENT_VALIDATION_PATIENT_NULL_MESSAGE, groups = {Create.class})
    private PatientDto patientDto;

    public interface Create {
    }

    public interface Read {
    }
}
