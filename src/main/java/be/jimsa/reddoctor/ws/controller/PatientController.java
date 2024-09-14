package be.jimsa.reddoctor.ws.controller;

import be.jimsa.reddoctor.config.document.annotation.PublicIdParameterDocument;
import be.jimsa.reddoctor.config.document.annotation.ReadMyAppointmentsRequestDocument;
import be.jimsa.reddoctor.config.document.annotation.UpdateAnAppointmentRequestDocument;
import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.dto.ResponseDto;
import be.jimsa.reddoctor.ws.service.PatientService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(path = PATIENT_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(name = PATIENT_DOCUMENT_NAME, description = PATIENT_DOCUMENT_DESCRIPTION)
public class PatientController {

    private final PatientService patientService;

    @PostMapping(PATIENT_POST_PATH)
    @UpdateAnAppointmentRequestDocument(summary = PATIENT_DOCUMENT_POST_SUMMERY, description = PATIENT_DOCUMENT_POST_DESCRIPTION)
    public ResponseEntity<ResponseDto> updateAnAppointment(
            @PublicIdParameterDocument @ValidPublicId @PathVariable(GENERAL_PUBLIC_ID_FIELD) String publicId,
            @Valid @RequestBody PatientDto patientDto
    ) {
        AppointmentDto updatedAppointmentDto = patientService.updateAnAppointment(publicId, patientDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(updatedAppointmentDto)
                                .build()
                );
    }

    @GetMapping(PATIENT_GET_PATH)
    @ReadMyAppointmentsRequestDocument(summary = PATIENT_DOCUMENT_GET_SUMMERY, description = PATIENT_DOCUMENT_GET_DESCRIPTION)
    public ResponseEntity<ResponseDto> getMyAppointments(

            @PathVariable(PATIENT_PHONE_NUMBER_FIELD) String phoneNumber,

            @Pattern(regexp = GENERAL_VALIDATION_TYPE_PATTERN, message = GENERAL_VALIDATION_TYPE_FIELD_PATTERN_MESSAGE)
            @RequestParam(value = GENERAL_TYPE_FIELD, defaultValue = APPOINTMENT_TYPE_FIELD_DEFAULT_VALUE)
            @Parameter(example = GENERAL_TYPE_FIELD, description = GENERAL_DOCUMENT_TYPE_FIELD_PATTERN_MESSAGE)
            String type,

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
        List<AppointmentDto> appointmentDtos = patientService.readMyAppointments(
                phoneNumber, page, size, type, sortDirection
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
}
