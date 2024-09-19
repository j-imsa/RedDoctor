package be.jimsa.reddoctor.unit.utility;


import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class AppointmentUtilsTests {

    private PublicIdGenerator publicIdGenerator;
    private AppointmentUtils appointmentUtils;

    @BeforeEach
    void setUp() {
        publicIdGenerator = new PublicIdGenerator();
        appointmentUtils = new AppointmentUtils(publicIdGenerator);
    }


    private Appointment getAppointment() {
        return Appointment.builder()
                .id(100L)
                .publicId(PUBLIC_ID_EXAMPLE_1)
                .date(LocalDate.of(2024, 9, 10))
                .startTime(LocalTime.of(10, 15, 20))
                .endTime(LocalTime.of(20, 30, 40))
                .status(Status.OPEN)
                .build();
    }

    private AppointmentDto getAppointmentDto() {
        return AppointmentDto.builder()
                .publicId(PUBLIC_ID_EXAMPLE_1)
                .date(LocalDate.of(2024, 9, 10))
                .start(LocalTime.of(10, 15, 20))
                .end(LocalTime.of(20, 30, 40))
                .status(Status.OPEN)
                .build();
    }

    private Patient getPatient() {
        return Patient.builder()
                .id(100L)
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }

    private PatientDto getPatientDto() {
        return PatientDto.builder()
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }


    @Nested
    @DisplayName("MapToEntity")
    class MapToEntityTests {

        @Test
        @DisplayName("with valid dto (null patient), should return valid entity")
        void testMapToEntityWithValidDto() {
            AppointmentDto appointmentDto = getAppointmentDto();

            Appointment appointment = appointmentUtils.mapToEntity(appointmentDto);

            assertThat(appointment)
                    .isNotNull();
            assertThat(appointment.getId())
                    .isNull();
            assertThat(appointment.getPublicId())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getPublicId());
            assertThat(appointment.getDate())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getDate());
            assertThat(appointment.getStartTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStart());
            assertThat(appointment.getEndTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getEnd());
            assertThat(appointment.getStatus())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStatus());
            assertThat(appointment.getPatient())
                    .isNull();
        }

        // This test case is optional because, in the business, we don't have mapToEntity by appointmentDto which has patientDto!
        // It means all the appointmentDtos have null-patientDto
        @Test
        @DisplayName("with valid dto (with patient), should return valid entity")
        void testMapToEntityWithValidDtoWithPatient() {
            AppointmentDto appointmentDto = getAppointmentDto();
            PatientDto patientDto = getPatientDto();
            appointmentDto.setPatientDto(patientDto);

            Appointment appointment = appointmentUtils.mapToEntity(appointmentDto);

            assertThat(appointment)
                    .isNotNull();
            assertThat(appointment.getId())
                    .isNull();
            assertThat(appointment.getPublicId())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getPublicId());
            assertThat(appointment.getDate())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getDate());
            assertThat(appointment.getStartTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStart());
            assertThat(appointment.getEndTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getEnd());
            assertThat(appointment.getStatus())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStatus());
            assertThat(appointment.getPatient())
                    .isNotNull();
            assertThat(appointment.getPatient().getName())
                    .isNotNull()
                    .isEqualTo(patientDto.getName());
            assertThat(appointment.getPatient().getPhoneNumber())
                    .isNotNull()
                    .isEqualTo(patientDto.getPhoneNumber());
        }

        @Test
        @DisplayName("with null dto, should throw NullPointerException")
        void testMapToEntityWithNullDto() {
            assertThatThrownBy(() -> appointmentUtils.mapToEntity(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
        }

        @Test
        @DisplayName("with null values, should return entity with null values")
        void testMapToEntityWithNullValues() {
            AppointmentDto appointmentDto = getAppointmentDto();
            appointmentDto.setPublicId(null);
            appointmentDto.setDate(null);
            appointmentDto.setStart(null);
            appointmentDto.setEnd(null);
            appointmentDto.setStatus(null);

            Appointment appointment = appointmentUtils.mapToEntity(appointmentDto);

            assertThat(appointment)
                    .isNotNull();
            assertThat(appointment.getId())
                    .isNull();
            assertThat(appointment.getPatient())
                    .isNull();
            assertThat(appointment.getPublicId())
                    .isNull();
            assertThat(appointment.getDate())
                    .isNull();
            assertThat(appointment.getStartTime())
                    .isNull();
            assertThat(appointment.getEndTime())
                    .isNull();
            assertThat(appointment.getStatus())
                    .isNull();
        }

        @Test
        @DisplayName("with invalid values, should return entity with invalid values ")
        void testMapToEntityWithInvalidValues() {
            AppointmentDto appointmentDto = getAppointmentDto();
            appointmentDto.setPublicId("Invalid PublicId @#$%^&*()");
            appointmentDto.setDate(LocalDate.MIN);
            appointmentDto.setStart(LocalTime.MAX);
            appointmentDto.setEnd(LocalTime.MIN);
            appointmentDto.setStatus(Status.DELETED);

            Appointment appointment = appointmentUtils.mapToEntity(appointmentDto);

            assertThat(appointment)
                    .isNotNull();
            assertThat(appointment.getPublicId())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getPublicId());
            assertThat(appointment.getDate())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getDate());
            assertThat(appointment.getStartTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStart());
            assertThat(appointment.getEndTime())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getEnd());
            assertThat(appointment.getStatus())
                    .isNotNull()
                    .isEqualTo(appointmentDto.getStatus());
            assertThat(appointment.getPatient())
                    .isNull();

        }
    }

    @Nested
    @DisplayName("MapToDto")
    class MapToDtoTests {

        @Test
        @DisplayName("with valid entity (null patient), should return valid dto")
        void testMapToDtoWithValidEntity() {
            Appointment appointment = getAppointment();

            AppointmentDto appointmentDto = appointmentUtils.mapToDto(appointment);

            assertThat(appointmentDto)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", appointment.getPublicId())
                    .hasFieldOrPropertyWithValue("date", appointment.getDate())
                    .hasFieldOrPropertyWithValue("start", appointment.getStartTime())
                    .hasFieldOrPropertyWithValue("end", appointment.getEndTime())
                    .hasFieldOrPropertyWithValue("status", appointment.getStatus())
                    .hasFieldOrPropertyWithValue("patientDto", null);
        }

        @Test
        @DisplayName("with valid entity (with patient), should return valid dto")
        void testMapToDtoWithValidEntityAndPatient() {
            Appointment appointment = getAppointment();
            Patient patient = getPatient();
            appointment.setPatient(patient);

            AppointmentDto appointmentDto = appointmentUtils.mapToDto(appointment);

            assertThat(appointmentDto)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", appointment.getPublicId())
                    .hasFieldOrPropertyWithValue("date", appointment.getDate())
                    .hasFieldOrPropertyWithValue("start", appointment.getStartTime())
                    .hasFieldOrPropertyWithValue("end", appointment.getEndTime())
                    .hasFieldOrPropertyWithValue("status", appointment.getStatus());
            assertThat(appointmentDto.getPatientDto())
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("name", patient.getName())
                    .hasFieldOrPropertyWithValue("phoneNumber", patient.getPhoneNumber());
        }

        @Test
        @DisplayName("with null entity, should throw NullPointerException")
        void testMapToDtoWithNullEntity() {
            assertThatThrownBy(() -> appointmentUtils.mapToDto(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
        }

        @Test
        @DisplayName("with null values, should return valid dto with null values")
        void testMapToDtoWithNullValues() {
            Appointment appointment = getAppointment();
            appointment.setId(null);
            appointment.setPublicId(null);
            appointment.setDate(null);
            appointment.setStatus(null);
            appointment.setStartTime(null);
            appointment.setEndTime(null);
            appointment.setPatient(null);

            AppointmentDto appointmentDto = appointmentUtils.mapToDto(appointment);

            assertThat(appointmentDto)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", appointment.getPublicId())
                    .hasFieldOrPropertyWithValue("date", appointment.getDate())
                    .hasFieldOrPropertyWithValue("start", appointment.getStartTime())
                    .hasFieldOrPropertyWithValue("end", appointment.getEndTime())
                    .hasFieldOrPropertyWithValue("status", appointment.getStatus());
            assertThat(appointmentDto.getPatientDto())
                    .isNull();
        }

        @Test
        @DisplayName("with invalid values, should return valid dto with invalid values")
        void testMapToDtoWithInvalidValues() {
            Appointment appointment = getAppointment();
            appointment.setId(-500L);
            appointment.setPublicId("Invalid PublicId !@#$%^&*()");
            appointment.setDate(LocalDate.MIN);
            appointment.setStatus(Status.DELETED); // Status has not invalid value!
            appointment.setStartTime(LocalTime.MAX);
            appointment.setEndTime(LocalTime.MIN);
            Patient patient = getPatient();
            patient.setId(-100L);
            patient.setName("I");
            patient.setPhoneNumber("1234");
            appointment.setPatient(patient);

            AppointmentDto appointmentDto = appointmentUtils.mapToDto(appointment);

            assertThat(appointmentDto)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", appointment.getPublicId())
                    .hasFieldOrPropertyWithValue("date", appointment.getDate())
                    .hasFieldOrPropertyWithValue("start", appointment.getStartTime())
                    .hasFieldOrPropertyWithValue("end", appointment.getEndTime())
                    .hasFieldOrPropertyWithValue("status", appointment.getStatus());
            assertThat(appointmentDto.getPatientDto())
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("name", patient.getName())
                    .hasFieldOrPropertyWithValue("phoneNumber", patient.getPhoneNumber());
        }

    }

    @Nested
    @DisplayName("Splitter")
    class SplitterTests {

        @ParameterizedTest
        @CsvSource({
                "10:00:00, 10:00:00, 0",
                "10:00:00, 10:29:00, 0",
                "10:00:00, 10:30:00, 1",
                "00:00:00, 00:30:00, 1",
                "23:29:00, 23:59:59, 1",
                "23:30:00, 23:59:59, 0",
                "23:31:00, 23:59:59, 0",
                "00:00:00, 23:59:59, 47"
        })
        @DisplayName("with valid times, should return a valid list")
        void testSplitterWithValidDto(String startStr, String endStr, int size) {
            AppointmentDto appointmentDto = getAppointmentDto();
            appointmentDto.setStart(LocalTime.parse(startStr));
            appointmentDto.setEnd(LocalTime.parse(endStr));

            List<AppointmentDto> appointmentDtos = appointmentUtils.splitter(appointmentDto);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(size)
                    .allMatch(dto -> dto.getStart().plusMinutes(GENERAL_DURATION).equals(dto.getEnd()));
        }

        @ParameterizedTest
        @CsvSource({
                "00:00:00, 00:00:00, 0",
                "00:00, 00:00, 0",
                "23:59:59, 10:10:10, 0",
                "23:59:59, 00:00:00, 0",
                "23:59, 00:00, 0",
        })
        @DisplayName("with invalid times, should return an empty list")
        void testSplitterWithValidDtoAndDifferentTime(String startStr, String endStr, int size) {
            AppointmentDto appointmentDto = getAppointmentDto();
            appointmentDto.setStart(LocalTime.parse(startStr));
            appointmentDto.setEnd(LocalTime.parse(endStr));

            List<AppointmentDto> appointmentDtos = appointmentUtils.splitter(appointmentDto);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(size);
        }

    }

    @Nested
    @DisplayName("PrintLogger")
    @ExtendWith(MockitoExtension.class)
    class PrintLoggerTests {

        @Mock
        private Logger logger;

        @Test
        @DisplayName("with valid data, should print 4 times info")
        void testPrintLoggerWithValidData() {
            Page<Appointment> appointmentPage = new PageImpl<>(
                    Collections.emptyList(),
                    PageRequest.of(0, 5),
                    10
            );

            appointmentUtils.printLogger(logger, appointmentPage);

            verify(logger).info(eq(LOGGER_TOTAL_ELEMENTS), eq(appointmentPage.getTotalElements()));
            verify(logger).info(eq(LOGGER_TOTAL_PAGES), eq(appointmentPage.getTotalPages()));
            verify(logger).info(eq(LOGGER_NUMBER_OF_ELEMENTS), eq(appointmentPage.getNumberOfElements()));
            verify(logger).info(eq(LOGGER_SIZE), eq(appointmentPage.getSize()));

        }

        @Test
        @DisplayName("with null data, should do nothing!")
        void testPrintLoggerWithNullData() {
            appointmentUtils.printLogger(null, null);

            verify(logger, times(0)).info(anyString(), anyString());
        }

    }

}
