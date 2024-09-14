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

    @GetMapping("/{" + APPOINTMENT_DATE_FIELD + "}")
    @ReadAllRequestDocument(summary = APPOINTMENT_DOCUMENT_GET_SUMMERY, description = APPOINTMENT_DOCUMENT_GET_DESCRIPTION)
    public ResponseEntity<ResponseDto> getAppointments(

            @JsonFormat(pattern = DATE_FORMAT)
            @PathVariable(APPOINTMENT_DATE_FIELD) String date,

            @Pattern(regexp = APPOINTMENT_VALIDATION_TYPE_PATTERN, message = APPOINTMENT_VALIDATION_TYPE_FIELD_PATTERN_MESSAGE)
            @RequestParam(value = APPOINTMENT_TYPE_FIELD, defaultValue = APPOINTMENT_TYPE_FIELD_DEFAULT_VALUE)
            @Parameter(example = APPOINTMENT_DOCUMENT_TYPE_FIELD, description = APPOINTMENT_DOCUMENT_TYPE_FIELD_PATTERN_MESSAGE)
            String type,

            @Positive(message = APPOINTMENT_VALIDATION_PAGE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = APPOINTMENT_PAGE_DEFAULT_VALUE)
            @Parameter(example = APPOINTMENT_DOCUMENT_PAGE_EXAMPLE)
            int page,

            @Positive(message = APPOINTMENT_VALIDATION_SIZE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = APPOINTMENT_SIZE_DEFAULT_VALUE)
            @Parameter(example = APPOINTMENT_DOCUMENT_SIZE_EXAMPLE)
            int size,

            @Pattern(regexp = APPOINTMENT_VALIDATION_SORT_DIRECTION_PATTERN, message = APPOINTMENT_VALIDATION_SORT_DIRECTION_PATTERN_MESSAGE)
            @RequestParam(value = APPOINTMENT_SORT_DIRECTION, defaultValue = APPOINTMENT_SORT_DIRECTION_DEFAULT_VALUE)
            @Parameter(example = APPOINTMENT_DOCUMENT_SORT_FIELD_DIRECTION_EXAMPLE, description = APPOINTMENT_DOCUMENT_SORT_DIRECTION_FIELD_DESCRIPTION)
            String sortDirection
    ) {
        List<AppointmentDto> appointmentDtos = appointmentService.readAppointments(
                date, page, size, type, sortDirection
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

    @DeleteMapping("/{" + APPOINTMENT_PUBLIC_ID_FIELD + "}")
    @DeleteAnAppointmentRequestDocument(summary = APPOINTMENT_DOCUMENT_DELETE_SUMMERY, description = APPOINTMENT_DOCUMENT_DELETE_DESCRIPTION)
    public ResponseEntity<ResponseDto> removeAProject(
            @PublicIdParameterDocument @ValidPublicId @PathVariable(APPOINTMENT_PUBLIC_ID_FIELD) String publicId
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
