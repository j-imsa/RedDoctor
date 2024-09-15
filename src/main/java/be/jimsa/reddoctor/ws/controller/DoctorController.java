package be.jimsa.reddoctor.ws.controller;

import be.jimsa.reddoctor.config.document.annotation.CreateAppointmentRequestDocument;
import be.jimsa.reddoctor.config.document.annotation.DeleteAnAppointmentRequestDocument;
import be.jimsa.reddoctor.config.document.annotation.PublicIdParameterDocument;
import be.jimsa.reddoctor.config.document.annotation.ReadAllRequestDocument;
import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import be.jimsa.reddoctor.ws.service.AppointmentService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@RestController
@RequestMapping(path = DOCTOR_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(name = DOCTOR_DOCUMENT_NAME, description = DOCTOR_DOCUMENT_DESCRIPTION)
public class DoctorController {

    private final AppointmentService appointmentService;

    @PostMapping
    @CreateAppointmentRequestDocument(summary = APPOINTMENT_DOCUMENT_POST_SUMMERY, description = APPOINTMENT_DOCUMENT_POST_DESCRIPTION)
    public ResponseEntity<ResponseDto> addAppointments(
            @Validated(AppointmentDto.Create.class) @RequestBody AppointmentDto appointmentDto
    ) {
        List<AppointmentDto> appointmentDtos = appointmentService.createAppointments(appointmentDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(appointmentDtos)
                                .build()
                );
    }

    @GetMapping(DOCTOR_GET_PATH)
    @ReadAllRequestDocument(summary = APPOINTMENT_DOCUMENT_GET_SUMMERY, description = APPOINTMENT_DOCUMENT_GET_DESCRIPTION)
    public ResponseEntity<ResponseDto> getAppointments(

            @JsonFormat(pattern = DATE_FORMAT)
            @PathVariable(GENERAL_DATE_FIELD) String date,

            @Pattern(regexp = GENERAL_VALIDATION_STATUS_PATTERN, message = GENERAL_VALIDATION_STATUS_FIELD_PATTERN_MESSAGE)
            @RequestParam(value = GENERAL_STATUS_FIELD, defaultValue = GENERAL_STATUS_OPEN)
            @Parameter(example = GENERAL_STATUS_FIELD, description = GENERAL_DOCUMENT_STATUS_FIELD_PATTERN_MESSAGE)
            String status,

            @Positive(message = GENERAL_VALIDATION_PAGE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = GENERAL_PAGE_DEFAULT_VALUE)
            @Parameter(example = GENERAL_DOCUMENT_PAGE_EXAMPLE)
            int page,

            @Positive(message = GENERAL_VALIDATION_SIZE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = GENERAL_SIZE_DEFAULT_VALUE)
            @Parameter(example = GENERAL_DOCUMENT_SIZE_EXAMPLE)
            int size,

            @Pattern(regexp = GENERAL_VALIDATION_SORT_DIRECTION_PATTERN, message = GENERAL_VALIDATION_SORT_DIRECTION_PATTERN_MESSAGE)
            @RequestParam(value = GENERAL_SORT_DIRECTION, defaultValue = GENERAL_SORT_DIRECTION_ASC_FIELD)
            @Parameter(example = GENERAL_SORT_DIRECTION_DESC_FIELD, description = GENERAL_DOCUMENT_SORT_DIRECTION_FIELD_DESCRIPTION)
            String sortDirection
    ) {
        List<AppointmentDto> appointmentDtos = appointmentService.readAppointments(
                date, page, size, status, sortDirection
        );
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(appointmentDtos)
                                .build()
                );
    }

    @DeleteMapping(DOCTOR_DELETE_PATH)
    @DeleteAnAppointmentRequestDocument(summary = APPOINTMENT_DOCUMENT_DELETE_SUMMERY, description = APPOINTMENT_DOCUMENT_DELETE_DESCRIPTION)
    public ResponseEntity<ResponseDto> removeAProject(
            @PublicIdParameterDocument @ValidPublicId @PathVariable(GENERAL_PUBLIC_ID_FIELD) String publicId
    ) {
        boolean result = appointmentService.removeAnAppointment(publicId);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(result)
                                .build()
                );
    }

}
