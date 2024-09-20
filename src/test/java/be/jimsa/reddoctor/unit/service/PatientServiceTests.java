package be.jimsa.reddoctor.unit.service;

import be.jimsa.reddoctor.config.exception.AppServiceException;
import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.mapper.PatientMapper;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.repository.PatientRepository;
import be.jimsa.reddoctor.ws.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class PatientServiceTests {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private AppointmentUtils appointmentUtils;

    private AppointmentDto generateAppointmentDto() {
        return AppointmentDto.builder()
                .publicId(PUBLIC_ID_EXAMPLE_1)
                .date(LocalDate.now())
                .start(LocalTime.now().truncatedTo(ChronoUnit.SECONDS))
                .end(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(GENERAL_DURATION))
                .build();
    }

    private List<AppointmentDto> generateAppointmentDtos() {
        List<AppointmentDto> dtos = new ArrayList<>();
        dtos.add(generateAppointmentDto());
        dtos.add(AppointmentDto.builder()
                .publicId(PUBLIC_ID_EXAMPLE_2)
                .date(LocalDate.now())
                .start(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(1L))
                .end(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(GENERAL_DURATION).plusHours(1L))
                .build());
        return dtos;
    }

    private Appointment generateAppointment() {
        return Appointment.builder()
                .id(100L)
                .publicId(PUBLIC_ID_EXAMPLE_1)
                .date(LocalDate.now())
                .startTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(GENERAL_DURATION))
                .build();
    }

    private List<Appointment> generateAppointments() {
        List<Appointment> entities = new ArrayList<>();
        entities.add(generateAppointment());
        entities.add(Appointment.builder()
                .id(200L)
                .publicId(PUBLIC_ID_EXAMPLE_2)
                .date(LocalDate.now())
                .startTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(1L))
                .endTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(GENERAL_DURATION).plusHours(1L))
                .build());
        return entities;
    }

    private Page<Appointment> generateAppointmentPage(int page, int size, int total) {
        return new PageImpl<>(
                generateAppointments(),
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD)),
                total
        );
    }

    private PatientDto generatePatientDto() {
        return PatientDto.builder()
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }

    private Patient generatePatient() {
        return Patient.builder()
                .id(100L)
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }

    @Nested
    @DisplayName("UpdateAnAppointment")
    class UpdateAnAppointmentTests {

        @Test
        @DisplayName("with valid public_id and patient, with exist patient, should return valid dto")
        void testUpdateAnAppointmentWithValidDataAndExistingPatient() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.of(generatePatient()));
            given(patientMapper.mapToEntityById(any(), anyLong())).willReturn(generatePatient());
            given(appointmentRepository.save(any())).willReturn(appointment);
            AppointmentDto appointmentDto = generateAppointmentDto();
            appointmentDto.setPatientDto(generatePatientDto());
            given(appointmentUtils.mapToDto(any())).willReturn(appointmentDto);

            AppointmentDto result = patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto());

            assertThat(result).isNotNull();
            assertThat(result.getPatientDto())
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("name", appointmentDto.getPatientDto().getName());
        }

        @Test
        @DisplayName("with valid public_id and patient, with new patient, should return valid dto")
        void testUpdateAnAppointmentWithValidDataAndNewPatient() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.empty());
            given(patientMapper.mapToEntity(any())).willReturn(generatePatient());
            given(appointmentRepository.save(any())).willReturn(appointment);
            AppointmentDto appointmentDto = generateAppointmentDto();
            appointmentDto.setPatientDto(generatePatientDto());
            given(appointmentUtils.mapToDto(any())).willReturn(appointmentDto);

            AppointmentDto result = patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto());

            assertThat(result).isNotNull();
            assertThat(result.getPatientDto())
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("name", appointmentDto.getPatientDto().getName());
        }

        @Test
        @DisplayName("with invalid public_id, should throw AppServiceException")
        void testUpdateAnAppointmentWithInvalidPublicId() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto()))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }

        @Test
        @DisplayName("with null public_id, should throw AppServiceException")
        void testUpdateAnAppointmentWithNullPublicId() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(any())).willReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.updateAnAppointment(null, generatePatientDto()))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }

        @Test
        @DisplayName("with null patient, should throw NullPointerException")
        void testUpdateAnAppointmentWithNullPatient() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));

            assertThatThrownBy(() -> patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
        }

        @Test
        @DisplayName("with taken status, should throw AppServiceException")
        void testUpdateAnAppointmentWithTakenStatus() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.TAKEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));

            assertThatThrownBy(() -> patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto()))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE);
        }

        @Test
        @DisplayName("with deleted status, should throw AppServiceException")
        void testUpdateAnAppointmentWithDeletedStatus() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.DELETED);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));

            assertThatThrownBy(() -> patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto()))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_RESOURCE_ALREADY_DELETED_MESSAGE);
        }

        @Test
        @DisplayName("transactional? should rollback")
        void testUpdateAnAppointmentTransactional() {
            Long initDbCount = appointmentRepository.count();
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.empty());
            given(patientMapper.mapToEntity(any())).willReturn(generatePatient());
            doAnswer(invocation -> {
                Appointment savedAppointment = (Appointment) invocation.getArgument(0);
                throw new RuntimeException("transactional");
            }).when(appointmentRepository).save(appointment);

            assertThatThrownBy(() -> patientService.updateAnAppointment(PUBLIC_ID_EXAMPLE_1, generatePatientDto()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("transactional");
            assertThat(appointmentRepository.count()).isEqualTo(initDbCount);
        }

    }

    @Nested
    @DisplayName("ReadMyAppointments")
    class ReadMyAppointmentsTests {

        @Test
        @DisplayName("with valid data, should return valid list")
        void testReadMyAppointmentsWithValidData() {
            String phoneNumber = "9131231234";
            int page = 1;
            int size = 20;
            String sortDirection = "asc";
            int expectedSize = 2;
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.of(generatePatient()));
            given(appointmentRepository.findAllByPatient(any(), any())).willReturn(generateAppointmentPage(page, size, 2 * size));
            given(appointmentUtils.mapToDto(any())).willReturn(generateAppointmentDto());

            List<AppointmentDto> appointmentDtos = patientService.readMyAppointments(phoneNumber, page, size, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
        }

        @Test
        @DisplayName("with invalid phone number, should return valid-empty list")
        void testReadMyAppointmentsWithInvalidPhoneNumber() {
            String phoneNumber = "9131231234";
            int page = 1;
            int size = 20;
            String sortDirection = "asc";
            int expectedSize = 0;
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.empty());

            List<AppointmentDto> appointmentDtos = patientService.readMyAppointments(phoneNumber, page, size, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
        }

        @Test
        @DisplayName("with invalid page, should throw IllegalArgumentException")
        void testReadMyAppointmentsWithInvalidPage() {
            String phoneNumber = "9131231234";
            int page = -100;
            int size = 20;
            String sortDirection = "asc";
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.of(generatePatient()));

            assertThatThrownBy(() ->patientService.readMyAppointments(phoneNumber, page, size, sortDirection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Page index must not be less than zero");
        }

        @Test
        @DisplayName("with invalid page size, should throw IllegalArgumentException")
        void testReadMyAppointmentsWithInvalidPageSize() {
            String phoneNumber = "9131231234";
            int page = 1;
            int size = -200;
            String sortDirection = "asc";
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.of(generatePatient()));

            assertThatThrownBy(() ->patientService.readMyAppointments(phoneNumber, page, size, sortDirection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Page size must not be less than one");
        }

        @Test
        @DisplayName("with valid patient and no appointment, should return valid-empty list")
        void testReadMyAppointmentsWithValidDataAndNoAppointment() {
            String phoneNumber = "9131231234";
            int page = 1;
            int size = 20;
            String sortDirection = "asc";
            int expectedSize = 0;
            given(patientRepository.findByPhoneNumber(anyString())).willReturn(Optional.of(generatePatient()));
            given(appointmentRepository.findAllByPatient(any(), any())).willReturn(new PageImpl<>(Collections.emptyList()));

            List<AppointmentDto> appointmentDtos = patientService.readMyAppointments(phoneNumber, page, size, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
        }

    }

}
