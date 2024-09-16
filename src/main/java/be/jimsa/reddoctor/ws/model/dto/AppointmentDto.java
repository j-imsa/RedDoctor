package be.jimsa.reddoctor.ws.model.dto;

import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import be.jimsa.reddoctor.config.validation.annotation.ValidTimeSequence;
import be.jimsa.reddoctor.ws.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@ValidTimeSequence(
        currentTime = APPOINTMENT_VALIDATION_DTO_START_TIME_FIELD,
        nextTime = APPOINTMENT_VALIDATION_DTO_END_TIME_FIELD,
        message = APPOINTMENT_VALIDATION_SEQUENCE_TIME_MESSAGE,
        groups = {AppointmentDto.Create.class, AppointmentDto.Read.class}
)
public class AppointmentDto {

    @JsonProperty(GENERAL_PUBLIC_ID_FIELD)
    @Null(message = APPOINTMENT_VALIDATION_PUBLIC_ID_NULL_MESSAGE, groups = {Create.class})
    @ValidPublicId(groups = Read.class)
    @Schema(type = GENERAL_STRING_TYPE, description = PUBLIC_ID_DESCRIPTION, example = PUBLIC_ID_EXAMPLE_1)
    private String publicId;

    @JsonFormat(pattern = DATE_FORMAT)
    @NotNull(message = GENERAL_VALIDATION_DATE_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = GENERAL_STRING_TYPE, example = DATE_FORMAT_EXAMPLE, description = DATE_FORMAT)
    private LocalDate date;

    @JsonFormat(pattern = TIME_FORMAT)
    @NotNull(message = APPOINTMENT_VALIDATION_START_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = GENERAL_STRING_TYPE, example = TIME_FORMAT_EXAMPLE, description = TIME_FORMAT)
    private LocalTime start;

    @JsonFormat(pattern = TIME_FORMAT)
    @NotNull(message = APPOINTMENT_VALIDATION_END_MESSAGE, groups = {Create.class, Read.class})
    @Schema(type = GENERAL_STRING_TYPE, example = TIME_FORMAT_EXAMPLE, description = TIME_FORMAT)
    private LocalTime end;

    @Null(message = APPOINTMENT_VALIDATION_TYPE_NULL_MESSAGE, groups = {Create.class})
    private Status status;

    @JsonProperty(PATIENT_FIELD)
    @Null(message = APPOINTMENT_VALIDATION_PATIENT_NULL_MESSAGE, groups = {Create.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PatientDto patientDto;

    public interface Create {
    }

    public interface Read {
    }

}
