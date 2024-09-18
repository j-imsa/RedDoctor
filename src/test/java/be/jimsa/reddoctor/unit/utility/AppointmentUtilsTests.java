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

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_EXAMPLE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    }

    @Nested
    @DisplayName("Splitter")
    class SplitterTests {

    }

    @Nested
    @DisplayName("PrintLogger")
    class PrintLoggerTests {

    }

}
