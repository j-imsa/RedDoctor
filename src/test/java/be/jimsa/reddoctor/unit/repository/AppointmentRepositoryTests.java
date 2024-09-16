package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.enums.Status;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Spy
    private PublicIdGenerator publicIdGenerator;

    @Nested
    @DisplayName("SaveAll")
    class SaveAllTests {

        @Test
        @DisplayName("by valid appointments, should return a list of saved entities with IDs")
        void testSaveAllValidAppointments() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalDate date2 = LocalDate.of(2024, 6, 20);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime start2 = LocalTime.of(12, 20);
            LocalTime end1 = LocalTime.of(11, 15);
            LocalTime end2 = LocalTime.of(13, 0);
            Status status1 = Status.OPEN;
            Status status2 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_2)
                    .date(date2)
                    .startTime(start2)
                    .endTime(end2)
                    .status(status2)
                    .build();

            List<Appointment> appointments = List.of(appointment1, appointment2);

            // when (Act)
            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

            // then (Assert)
            assertThat(savedAppointments)
                    .isNotEmpty()
                    .hasSize(appointments.size())
                    .allMatch(appointment -> appointment.getId() != null && appointment.getId() > 0)
                    .allMatch(appointment -> appointment.getPublicId() != null)
                    .allMatch(appointment -> appointment.getPatient() == null)
                    .extracting(Appointment::getDate)
                    .containsExactly(date1, date2);

            assertThat(savedAppointments)
                    .extracting(Appointment::getStartTime)
                    .containsExactly(start1, start2);

            assertThat(savedAppointments)
                    .extracting(Appointment::getEndTime)
                    .containsExactly(end1, end2);

            assertThat(savedAppointments)
                    .extracting(Appointment::getStatus)
                    .containsExactly(status1, status2);

            assertThat(savedAppointments)
                    .extracting(Appointment::getPatient)
                    .containsOnlyNulls();
        }

        @Test
        @DisplayName("with an empty list, should return an empty list")
        void testSaveAllEmptyList() {
            // given (Arrange)
            List<Appointment> emptyList = Collections.emptyList();

            // when (Act)
            List<Appointment> savedAppointments = appointmentRepository.saveAll(emptyList);

            // then (Assert)
            assertThat(savedAppointments)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("with a list containing null, should throw InvalidDataAccessApiUsageException and stop the process")
        void testSaveAllWithNullEntity() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalDate date2 = LocalDate.of(2024, 6, 20);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime start2 = LocalTime.of(12, 20);
            LocalTime end1 = LocalTime.of(11, 15);
            LocalTime end2 = LocalTime.of(13, 0);
            Status status1 = Status.OPEN;
            Status status2 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_2)
                    .date(date2)
                    .startTime(start2)
                    .endTime(end2)
                    .status(status2)
                    .build();

            List<Appointment> appointments = new ArrayList<>();
            appointments.add(appointment1);
            appointments.add(null); // Adding a null entity
            appointments.add(appointment2); // This item will not be processed!

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class)
                    .hasMessageContaining("Entity must not be null");

            // verify no appointments were saved
            entityManager.clear();
            assertThat(appointmentRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("with invalid appointments, should not save and throw ConstraintViolationException")
        void testSaveAllWithInvalidAppointments() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(14, 15); // Invalid time: startTime is after endTime
            LocalTime end1 = LocalTime.of(11, 15);
            Status status1 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining(APPOINTMENT_VALIDATION_SEQUENCE_TIME_MESSAGE);
        }

        @ParameterizedTest
        @EnumSource(value = Status.class, names = {"OPEN", "DELETED", "TAKEN"})
        @DisplayName("with valid status appointment, should save the entity with the status")
        void testSaveAllWithValidStatusAppointment(Status status) {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime end1 = LocalTime.of(11, 15);

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when (Act)
            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

            // then (Assert)
            assertThat(savedAppointments)
                    .isNotEmpty()
                    .hasSize(1)
                    .allMatch(appointment -> appointment.getStatus() == status);
        }

        @Test
        @DisplayName("with duplicate publicId, should throw DataIntegrityViolationException")
        void testSaveAllWithDuplicatePublicId() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalDate date2 = LocalDate.of(2024, 6, 20);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime start2 = LocalTime.of(12, 20);
            LocalTime end1 = LocalTime.of(11, 15);
            LocalTime end2 = LocalTime.of(13, 0);
            Status status1 = Status.OPEN;
            Status status2 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1) // Duplicate publicId
                    .date(date2)
                    .startTime(start2)
                    .endTime(end2)
                    .status(status2)
                    .build();

            List<Appointment> appointments = List.of(appointment1, appointment2);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with null publicId, should throw ConstraintViolationException")
        void testSaveAllWithNullPublicId() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime end1 = LocalTime.of(11, 15);
            Status status1 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(null)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Validation failed for");
        }

        @Test
        @DisplayName("with null date, should throw DataIntegrityViolationException")
        void testSaveAllWithNullDate() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime end1 = LocalTime.of(11, 15);
            Status status1 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(null)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with null start time, should throw ValidationException")
        void testSaveAllWithNullStartTime() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime end1 = LocalTime.of(11, 15);
            Status status1 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(null)
                    .endTime(end1)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("with null end time, should throw ValidationException")
        void testSaveAllWithNullEndTime() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime end1 = LocalTime.of(11, 15);
            Status status1 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(null)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("with mixed valid and invalid appointments, should save valid items, until exception")
        void testSaveAllWithMixedAppointments() {
            // given (Arrange)
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            LocalDate date2 = LocalDate.of(2024, 6, 20);
            LocalTime start1 = LocalTime.of(10, 15);
            LocalTime start2 = LocalTime.of(12, 20);
            LocalTime end1 = LocalTime.of(11, 15);
            LocalTime end2 = LocalTime.of(8, 0);
            Status status1 = Status.OPEN;
            Status status2 = Status.OPEN;

            Appointment appointment1 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_2)
                    .date(date2)
                    .startTime(start2)
                    .endTime(end2) // Invalid time
                    .status(status2)
                    .build();
            Appointment appointment3 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1 + PUBLIC_ID_EXAMPLE_2)
                    .date(date1)
                    .startTime(start1)
                    .endTime(end1)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment1, appointment2, appointment3);

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class);

            // verify no appointments were saved
            entityManager.clear();
            assertThat(appointmentRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("by a null as a list, should throw InvalidDataAccessApiUsageException")
        void testSaveAllWithNullAppointments() {
            // given (Arrange) - precondition or setup:
            List<Appointment> appointments = null;

            // when & then (Assert)
            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class);

        }

        @ParameterizedTest
        @CsvSource({
                "00:00, 00:30",
                "23:30, 00:00"
        })
        @DisplayName("with boundary times, should save all of them")
        void testSaveAllWithBoundaryTimes(String start, String end) {
            // given (Arrange) - precondition or setup:
            LocalDate date1 = LocalDate.of(2024, 9, 10);
            Status status1 = Status.OPEN;
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);

            Appointment appointment = Appointment.builder()
                    .publicId(publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH))
                    .date(date1)
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(status1)
                    .build();

            List<Appointment> appointments = List.of(appointment);

            // when (Act) - action or the behavior that we are going test:
            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

            // then(Assert) - verify the output:
            assertThat(savedAppointments)
                    .isNotNull()
                    .extracting(Appointment::getId)
                    .allMatch(id -> id != null && id > 0);

        }
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("by a valid entity, should return a valid saved entity with id")
        void testSaveWithValidEntity() {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .status(status)
                    .build();

            // when (Act) - action or the behavior that we are going test:
            Appointment savedAppointment = appointmentRepository.save(appointment);

            // then(Assert) - verify the output:
            assertThat(savedAppointment)
                    .isNotNull()
                    .isInstanceOf(Appointment.class)
                    .matches(entity -> entity.getId() != null && entity.getId() > 0)
                    .matches(entity -> entity.getPublicId().equals(PUBLIC_ID_EXAMPLE_1))
                    .matches(entity -> entity.getDate().equals(date))
                    .matches(entity -> entity.getStartTime().equals(start))
                    .matches(entity -> entity.getEndTime().equals(end))
                    .matches(entity -> entity.getStatus().equals(appointment.getStatus()));
        }

        @Test
        @DisplayName("by null public_id, should throw ConstraintViolationException")
        void testSaveWithNullPublicId() {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(null)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Invalid public_id");
        }

        @Test
        @DisplayName("by null date, should throw DataIntegrityViolationException")
        void testSaveWithNullDate() {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(null)
                    .startTime(start)
                    .endTime(end)
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("by null start time, should throw ValidationException")
        void testSaveWithNullStartTime() {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date)
                    .startTime(null)
                    .endTime(end)
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("by null end time, should throw ValidationException")
        void testSaveWithNullEndTime() {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date)
                    .startTime(start)
                    .endTime(null)
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("by null object, should throw InvalidDataAccessApiUsageException")
        void testSaveWithNullObject() {

            Appointment appointment = null;

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class)
                    .hasMessageContaining("Entity must not be null");
        }

        @Test
        @DisplayName("by invalid public_id, should throw ConstraintViolationException")
        void testSaveWithInvalidPublicId() {

            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1.split("-")[0]) // invalid
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Invalid public_id");
        }

        @Test
        @DisplayName("by invalid time sequence, should throw ConstraintViolationException")
        void testSaveWithInvalidImeSequence() {

            LocalDate date = LocalDate.of(2024, 9, 10);
            LocalTime start = LocalTime.of(10, 15);
            LocalTime end = LocalTime.of(11, 15);
            Status status = Status.OPEN;

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date)
                    .startTime(end) // end
                    .endTime(start) // start
                    .status(status)
                    .build();

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("The start time must BE before the end time!");
        }

        @ParameterizedTest
        @CsvSource({
                "00:00, 00:30",
                "23:30, 00:00"
        })
        @DisplayName("with boundary times, should save all of them")
        void testSaveWithBoundaryTimes(String start, String end) {
            // given (Arrange) - precondition or setup:
            LocalDate date = LocalDate.of(2024, 9, 10);
            Status status = Status.OPEN;
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);

            Appointment appointment = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_1)
                    .date(date)
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(status)
                    .build();


            // when (Act) - action or the behavior that we are going test:
            Appointment savedAppointment = appointmentRepository.save(appointment);

            // then(Assert) - verify the output:
            assertThat(savedAppointment)
                    .isNotNull()
                    .extracting(Appointment::getId)
                    .matches(id -> id != null && id > 0);

        }

    }

    @Nested
    @DisplayName("FindAllByDate")
    class FindAllByDateTests {

        @Nested
        @DisplayName("ByPageable")
        class ByPageableTests {

        }

        @Nested
        @DisplayName("WithoutPageable")
        class WithoutPageableTests {

        }
    }

    @Nested
    @DisplayName("FindByPublicId")
    class FindByPublicIdTests {

    }

    @Nested
    @DisplayName("FindAllByDateAndStatus")
    class FindAllByDateAndStatusTests {

    }

    @Nested
    @DisplayName("FindAllByPatientIsNullAndDate")
    class FindAllByPatientIsNullAndDateTests {

    }

    @Nested
    @DisplayName("FindAllByPatient")
    class FindAllByPatientTests {

    }

}
