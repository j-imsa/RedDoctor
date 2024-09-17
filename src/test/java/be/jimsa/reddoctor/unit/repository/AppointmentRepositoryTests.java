package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.repository.PatientRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private List<Appointment> getTwoAppointments() {
        int year = 2024;
        int month = 9;
        int day = 20;

        // 10:15:20
        int startHour1 = 10;
        int startMinute1 = 15;
        int startSecond1 = 20;

        // 12:20:30
        int endHour1 = 12;
        int endMinute1 = 20;
        int endSecond1 = 30;

        Status status1 = Status.OPEN;

        LocalDate date1 = LocalDate.of(year, month, day);
        LocalTime start1 = LocalTime.of(startHour1, startMinute1, startSecond1);
        LocalTime end1 = LocalTime.of(endHour1, endMinute1, endSecond1);

        Appointment appointment1 = Appointment.builder()
                .publicId(PUBLIC_ID_EXAMPLE_1)
                .date(date1)
                .startTime(start1)
                .endTime(end1)
                .status(status1)
                .build();

        // 18:15:20
        int startHour2 = 18;
        int startMinute2 = 15;
        int startSecond2 = 20;

        // 22:20:30
        int endHour2 = 22;
        int endMinute2 = 20;
        int endSecond2 = 30;

        Status status2 = Status.OPEN;

        LocalDate date2 = LocalDate.of(year, month, day);
        LocalTime start2 = LocalTime.of(startHour2, startMinute2, startSecond2);
        LocalTime end2 = LocalTime.of(endHour2, endMinute2, endSecond2);

        Appointment appointment2 = Appointment.builder()
                .publicId(PUBLIC_ID_EXAMPLE_2)
                .date(date2)
                .startTime(start2)
                .endTime(end2)
                .status(status2)
                .build();

        return List.of(appointment1, appointment2);
    }

    @Nested
    @DisplayName("SaveAll")
    class SaveAllTests {

        @Test
        @DisplayName("by valid appointments, should return a list of saved entities with IDs")
        void testSaveAllValidAppointments() {
            List<Appointment> appointments = getTwoAppointments();

            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

            assertThat(savedAppointments)
                    .isNotEmpty()
                    .hasSize(appointments.size())
                    .allMatch(appointment -> appointment.getId() != null && appointment.getId() > 0)
                    .allMatch(appointment -> appointment.getPublicId() != null)
                    .allMatch(appointment -> appointment.getPatient() == null)
                    .extracting(Appointment::getDate)
                    .containsExactly(appointments.get(0).getDate(), appointments.get(1).getDate());

            assertThat(savedAppointments)
                    .extracting(Appointment::getStartTime)
                    .containsExactly(appointments.get(0).getStartTime(), appointments.get(1).getStartTime());

            assertThat(savedAppointments)
                    .extracting(Appointment::getEndTime)
                    .containsExactly(appointments.get(0).getEndTime(), appointments.get(1).getEndTime());

            assertThat(savedAppointments)
                    .extracting(Appointment::getStatus)
                    .containsExactly(appointments.get(0).getStatus(), appointments.get(1).getStatus());

            assertThat(savedAppointments)
                    .extracting(Appointment::getPatient)
                    .containsOnlyNulls();
        }

        @Test
        @DisplayName("with an empty list, should return an empty list")
        void testSaveAllEmptyList() {
            List<Appointment> emptyList = Collections.emptyList();

            List<Appointment> savedAppointments = appointmentRepository.saveAll(emptyList);

            assertThat(savedAppointments)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("with a list containing null, should throw InvalidDataAccessApiUsageException and stop the process")
        void testSaveAllWithNullEntity() {
            List<Appointment> appointments = new ArrayList<>();
            appointments.add(getTwoAppointments().get(0));
            appointments.add(null); // Adding a null entity
            appointments.add(getTwoAppointments().get(1)); // This item will not be processed!

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class)
                    .hasMessageContaining("Entity must not be null");

            entityManager.clear();
            assertThat(appointmentRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("with invalid appointments, should not save and throw ConstraintViolationException")
        void testSaveAllWithInvalidAppointments() {
            LocalTime invalidStartTime = LocalTime.of(14, 15); // Invalid time: startTime is after endTime
            LocalTime endTime = LocalTime.of(11, 15);

            List<Appointment> appointments = getTwoAppointments();
            appointments.get(0).setStartTime(invalidStartTime);
            appointments.get(0).setEndTime(endTime);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining(APPOINTMENT_VALIDATION_SEQUENCE_TIME_MESSAGE);
        }

        @ParameterizedTest
        @EnumSource(value = Status.class, names = {"OPEN", "DELETED", "TAKEN"})
        @DisplayName("with valid status appointment, should save the entity with the status")
        void testSaveAllWithValidStatusAppointment(Status status) {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStatus(status);
            List<Appointment> appointments = List.of(appointment);

            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

            assertThat(savedAppointments)
                    .isNotEmpty()
                    .hasSize(1)
                    .allMatch(apt -> apt.getStatus().equals(status));
        }

        @Test
        @DisplayName("with duplicate publicId, should throw DataIntegrityViolationException")
        void testSaveAllWithDuplicatePublicId() {
            List<Appointment> appointments = getTwoAppointments();
            appointments.get(0).setPublicId(PUBLIC_ID_EXAMPLE_1);
            appointments.get(1).setPublicId(PUBLIC_ID_EXAMPLE_1);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with null publicId, should throw ConstraintViolationException")
        void testSaveAllWithNullPublicId() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setPublicId(null);
            List<Appointment> appointments = List.of(appointment);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Validation failed for");
        }

        @Test
        @DisplayName("with null date, should throw DataIntegrityViolationException")
        void testSaveAllWithNullDate() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setDate(null);
            List<Appointment> appointments = List.of(appointment);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with null start time, should throw ValidationException")
        void testSaveAllWithNullStartTime() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStartTime(null);
            List<Appointment> appointments = List.of(appointment);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("with null end time, should throw ValidationException")
        void testSaveAllWithNullEndTime() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setEndTime(null);
            List<Appointment> appointments = List.of(appointment);

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("with mixed valid and invalid appointments, should save valid items, until exception")
        void testSaveAllWithMixedAppointments() {
            LocalDate tmpDate = LocalDate.of(2024, 6, 20);
            LocalTime tmpStart = LocalTime.of(12, 20);
            LocalTime tmpEnd = LocalTime.of(8, 0); // Invalid time
            Status tmpStatus = Status.OPEN;
            Appointment appointment2 = Appointment.builder()
                    .publicId(PUBLIC_ID_EXAMPLE_2)
                    .date(tmpDate)
                    .startTime(tmpStart)
                    .endTime(tmpEnd)
                    .status(tmpStatus)
                    .build();
            List<Appointment> appointments = new ArrayList<>();
            appointments.add(getTwoAppointments().get(0));
            appointments.add(appointment2);
            appointments.add(getTwoAppointments().get(1));

            assertThatThrownBy(() -> appointmentRepository.saveAll(appointments))
                    .isInstanceOf(ConstraintViolationException.class);
            entityManager.clear();
            assertThat(appointmentRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("with null, should throw InvalidDataAccessApiUsageException")
        void testSaveAllWithNullAppointments() {
            assertThatThrownBy(() -> appointmentRepository.saveAll(null))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class);

        }

        @ParameterizedTest
        @CsvSource({"00:00, 00:30", "23:30, 00:00"})
        @DisplayName("with boundary times, should save all of them")
        void testSaveAllWithBoundaryTimes(String start, String end) {
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);
            List<Appointment> appointments = List.of(appointment);

            List<Appointment> savedAppointments = appointmentRepository.saveAll(appointments);

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
            Appointment appointment = getTwoAppointments().get(0);

            Appointment savedAppointment = appointmentRepository.save(appointment);

            assertThat(savedAppointment)
                    .isNotNull()
                    .isInstanceOf(Appointment.class)
                    .matches(entity -> entity.getId() != null && entity.getId() > 0)
                    .matches(entity -> entity.getPublicId().equals(PUBLIC_ID_EXAMPLE_1))
                    .matches(entity -> entity.getDate().equals(appointment.getDate()))
                    .matches(entity -> entity.getStartTime().equals(appointment.getStartTime()))
                    .matches(entity -> entity.getEndTime().equals(appointment.getEndTime()))
                    .matches(entity -> entity.getStatus().equals(appointment.getStatus()));
        }

        @Test
        @DisplayName("by null public_id, should throw ConstraintViolationException")
        void testSaveWithNullPublicId() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setPublicId(null);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Invalid public_id");
        }

        @Test
        @DisplayName("by null date, should throw DataIntegrityViolationException")
        void testSaveWithNullDate() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setDate(null);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("by null start time, should throw ValidationException")
        void testSaveWithNullStartTime() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStartTime(null);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("by null end time, should throw ValidationException")
        void testSaveWithNullEndTime() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setEndTime(null);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Unexpected exception during isValid call");
        }

        @Test
        @DisplayName("by null object, should throw InvalidDataAccessApiUsageException")
        void testSaveWithNullObject() {
            assertThatThrownBy(() -> appointmentRepository.save(null))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class)
                    .hasMessageContaining("Entity must not be null");
        }

        @Test
        @DisplayName("by invalid public_id, should throw ConstraintViolationException")
        void testSaveWithInvalidPublicId() {
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setPublicId(PUBLIC_ID_EXAMPLE_1.split("-")[0]);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Invalid public_id");
        }

        @Test
        @DisplayName("by invalid time sequence, should throw ConstraintViolationException")
        void testSaveWithInvalidImeSequence() {
            LocalTime start = LocalTime.of(12, 20);
            LocalTime end = LocalTime.of(11, 15);
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStartTime(start);
            appointment.setEndTime(end);

            assertThatThrownBy(() -> appointmentRepository.save(appointment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("The start time must BE before the end time!");
        }

        @ParameterizedTest
        @CsvSource({"00:00, 00:30", "23:30, 00:00"})
        @DisplayName("with boundary times, should save all of them")
        void testSaveWithBoundaryTimes(String start, String end) {
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);
            Appointment appointment = getTwoAppointments().get(0);
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);

            Appointment savedAppointment = appointmentRepository.save(appointment);

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
        @DisplayName("WithoutPageable")
        class WithoutPageableTests {

            @Test
            @DisplayName("with a valid date, should return valid list")
            void testFindAllByDateWithValidDate() {
                List<Appointment> appointments = getTwoAppointments();
                LocalDate date = appointments.get(0).getDate();
                appointmentRepository.saveAll(appointments);

                List<Appointment> savedAppointments = appointmentRepository.findAllByDate(date);

                assertThat(savedAppointments)
                        .isNotNull()
                        .hasSize(appointments.size())
                        .allMatch(appointment -> appointment.getDate().isEqual(date));
            }

            @Test
            @DisplayName("with a valid date and empty db, should return valid-empty list")
            void testFindAllByDateWithValidDateAndEmptyDb() {
                LocalDate date = LocalDate.of(2024, 9, 10);

                List<Appointment> savedAppointments = appointmentRepository.findAllByDate(date);

                assertThat(savedAppointments)
                        .isNotNull()
                        .hasSize(0);
            }

            @Test
            @DisplayName("with different date, should return empty list")
            void testFindAllByDateWithDifferentDate() {
                LocalDate date = LocalDate.of(2012, 9, 10);
                appointmentRepository.saveAll(getTwoAppointments());

                List<Appointment> savedAppointments = appointmentRepository.findAllByDate(date);

                assertThat(savedAppointments)
                        .isNotNull()
                        .hasSize(0);
            }

            @Test
            @DisplayName("with null, should return empty list")
            void testFindAllByDateWithNull() {

                List<Appointment> appointments = appointmentRepository.findAllByDate(null);

                assertThat(appointments)
                        .isNotNull()
                        .hasSize(0);
            }

        }

        @Nested
        @DisplayName("ByPageable")
        class ByPageableTests {

            @Test
            @DisplayName("with valid info, should return valid page")
            void testFindAllByDateWithValidInfo() {
                LocalDate date = getTwoAppointments().get(0).getDate();
                appointmentRepository.saveAll(getTwoAppointments());
                Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(pageable, date);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
                assertThat(appointmentPage.getSize()).isEqualTo(2);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));

            }

            @Test
            @DisplayName("with valid info and empty db, should return valid-empty page")
            void testFindAllByDateWithValidInfoAndEmptyDb() {
                LocalDate date = LocalDate.of(2024, 9, 10);
                Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(pageable, date);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
                assertThat(appointmentPage.getSize()).isEqualTo(2);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            }

            @Test
            @DisplayName("with different date, should return empty page")
            void testFindAllByDateWithDifferentDate() {
                LocalDate date = LocalDate.of(2011, 9, 10);
                appointmentRepository.saveAll(getTwoAppointments());
                Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(pageable, date);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
                assertThat(appointmentPage.getSize()).isEqualTo(2);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            }

            @Test
            @DisplayName("with null date, should return empty page")
            void testFindAllByDateWithNullDate() {
                appointmentRepository.saveAll(getTwoAppointments());
                Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(pageable, null);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
                assertThat(appointmentPage.getSize()).isEqualTo(2);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            }

            @Test
            @DisplayName("with null pageable, should return unsorted page")
            void testFindAllByDateWithNullPageable() {
                LocalDate date = getTwoAppointments().get(0).getDate();
                appointmentRepository.saveAll(getTwoAppointments());

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(null, date);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
                assertThat(appointmentPage.getSize()).isEqualTo(2);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
            }

            @Test
            @DisplayName("with null pageable and date, should return unsorted-empty page")
            void testFindAllByDateWithNullPageableAndNullDate() {
                appointmentRepository.saveAll(getTwoAppointments());

                Page<Appointment> appointmentPage = appointmentRepository.findAllByDate(null, null);

                assertThat(appointmentPage).isNotNull();
                assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
                assertThat(appointmentPage.getTotalPages()).isEqualTo(1); // page starts from one by default
                assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
                assertThat(appointmentPage.getSize()).isEqualTo(0);
                assertThat(appointmentPage.getPageable()).isNotNull();
                assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
            }

        }

    }

    @Nested
    @DisplayName("FindByPublicId")
    class FindByPublicIdTests {

        @Test
        @DisplayName("with valid info, should return valid entity")
        void testFindByPublicIdWithValidInfo() {
            Appointment appointment = appointmentRepository.saveAll(getTwoAppointments()).get(0);

            Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(PUBLIC_ID_EXAMPLE_1);

            assertThat(optionalAppointment)
                    .isPresent()
                    .contains(appointment)
                    .hasValueSatisfying(apt -> {
                        assertThat(apt).isNotNull();
                        assertThat(apt.getPublicId()).isEqualTo(PUBLIC_ID_EXAMPLE_1);
                        assertThat(apt.getId()).isGreaterThan(0L);
                        assertThat(apt)
                                .hasFieldOrPropertyWithValue("date", appointment.getDate())
                                .hasFieldOrPropertyWithValue("startTime", appointment.getStartTime())
                                .hasFieldOrPropertyWithValue("endTime", appointment.getEndTime())
                                .hasFieldOrPropertyWithValue("status", appointment.getStatus());
                    });
        }

        @Test
        @DisplayName("with valid info and empty db, should return valid-empty optional")
        void testFindByPublicIdWithValidInfoAndEmptyDb() {
            Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(PUBLIC_ID_EXAMPLE_1);

            assertThat(optionalAppointment)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("with null public_id, should return valid-empty optional")
        void testFindByPublicIdWithNull() {
            Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(null);

            assertThat(optionalAppointment)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("with invalid public_id, should return valid-empty optional")
        void testFindByPublicIdWithInvalidPublicId() {
            Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(PUBLIC_ID_EXAMPLE_1.split("-")[0]);

            assertThat(optionalAppointment)
                    .isNotNull()
                    .isEmpty();
        }

    }

    @Nested
    @DisplayName("FindAllByDateAndStatus")
    class FindAllByDateAndStatusTests {

        @Test
        @DisplayName("with valid info, should return valid page")
        void testFindAllByDateAndStatusWithValidInfo() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            LocalDate date = getTwoAppointments().get(0).getDate();
            Status status = Status.OPEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with valid info and empty db, should return valid-empty page")
        void testFindAllByDateAndStatusWithValidInfoAndEmptyDb() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            LocalDate date = LocalDate.of(2024, 9, 10);
            Status status = Status.OPEN;

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with invalid date, should return valid-empty page")
        void testFindAllByDateAndStatusWithInvalidDate() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            LocalDate date = LocalDate.of(2015, 9, 10);
            Status status = Status.OPEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with invalid status, should return valid-empty page")
        void testFindAllByDateAndStatusWithInvalidStatus() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            LocalDate date = LocalDate.of(2024, 9, 10);
            Status status = Status.TAKEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null pageable, should return valid page")
        void testFindAllByDateAndStatusWithNullPageable() {
            LocalDate date = getTwoAppointments().get(0).getDate();
            Status status = Status.OPEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(null, date, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
        }

        @Test
        @DisplayName("with null status, should return valid-empty page")
        void testFindAllByDateAndStatusWithNullStatus() {
            LocalDate date = LocalDate.of(2024, 9, 10);
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, null);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null date, should return valid-empty page")
        void testFindAllByDateAndStatusWithNullDate() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            Status status = Status.TAKEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, null, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null date and status, should return valid-empty page")
        void testFindAllByDateAndStatusWithNullDateAndStatus() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, null, null);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null date and pageable, should return valid-empty page")
        void testFindAllByDateAndStatusWithNullDateAndPageable() {
            Status status = Status.OPEN;
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(null, null, status);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(0);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
        }

        @Test
        @DisplayName("with null status and pageable, should return valid-empty page")
        void testFindAllByDateAndStatusWithNullStatusAndPageable() {
            LocalDate date = LocalDate.of(2024, 9, 10);
            appointmentRepository.saveAll(getTwoAppointments());

            Page<Appointment> appointmentPage = appointmentRepository.findAllByDateAndStatus(null, date, null);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(0);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
        }

    }

    @Nested
    @DisplayName("FindAllByPatient")
    class FindAllByPatientTests {

        @Test
        @DisplayName("with valid info, (one patient, two appointments), should return valid page")
        void testFindAllByPatientWithValidInfo1() {
            String pName1 = "Foo bar";
            String pPhoneNumber1 = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(pName1)
                    .phoneNumber(pPhoneNumber1)
                    .build();
            String pName2 = "Foo beer";
            String pPhoneNumber2 = "9121231234";
            Patient patient2 = Patient.builder()
                    .name(pName2)
                    .phoneNumber(pPhoneNumber2)
                    .build();
            List<Appointment> appointments = getTwoAppointments();
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            patientRepository.saveAll(List.of(patient1, patient2));
            appointments.get(0).setPatient(patient1);
            appointments.get(1).setPatient(patient1);
            appointmentRepository.saveAll(appointments);

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(pageable, patient1);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with valid info, (one patient, one appointment), should return valid page")
        void testFindAllByPatientWithValidInfo2() {
            String pName1 = "Foo bar";
            String pPhoneNumber1 = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(pName1)
                    .phoneNumber(pPhoneNumber1)
                    .build();
            String pName2 = "Foo beer";
            String pPhoneNumber2 = "9121231234";
            Patient patient2 = Patient.builder()
                    .name(pName2)
                    .phoneNumber(pPhoneNumber2)
                    .build();
            List<Appointment> appointments = getTwoAppointments();
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            patientRepository.saveAll(List.of(patient1, patient2));
            appointments.get(0).setPatient(patient1);
            appointments.get(1).setPatient(patient2);
            appointmentRepository.saveAll(appointments);

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(pageable, patient1);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(1);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(1);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with invalid patient, should return valid-empty page")
        void testFindAllByPatientWithInvalidPatient() {
            String pName1 = "Foo bar";
            String pPhoneNumber1 = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(pName1)
                    .phoneNumber(pPhoneNumber1)
                    .build();
            String pName2 = "Foo beer";
            String pPhoneNumber2 = "9121231234";
            Patient patient2 = Patient.builder()
                    .name(pName2)
                    .phoneNumber(pPhoneNumber2)
                    .build();
            List<Appointment> appointments = getTwoAppointments();
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            patientRepository.saveAll(List.of(patient1, patient2));
            appointments.get(0).setPatient(patient1);
            appointments.get(1).setPatient(patient1);
            appointmentRepository.saveAll(appointments);

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(pageable, patient2);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null pageable, should return valid-empty page")
        void testFindAllByPatientWithNullPageable() {
            String pName1 = "Foo bar";
            String pPhoneNumber1 = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(pName1)
                    .phoneNumber(pPhoneNumber1)
                    .build();
            String pName2 = "Foo beer";
            String pPhoneNumber2 = "9121231234";
            Patient patient2 = Patient.builder()
                    .name(pName2)
                    .phoneNumber(pPhoneNumber2)
                    .build();
            List<Appointment> appointments = getTwoAppointments();
            patientRepository.saveAll(List.of(patient1, patient2));
            appointments.get(0).setPatient(patient1);
            appointments.get(1).setPatient(patient1);
            appointmentRepository.saveAll(appointments);

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(null, patient1);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(2);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(2);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
        }

        @Test
        @DisplayName("with null patient, should return valid-empty page")
        void testFindAllByPatientWithNullPatient() {
            String pName1 = "Foo bar";
            String pPhoneNumber1 = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(pName1)
                    .phoneNumber(pPhoneNumber1)
                    .build();
            String pName2 = "Foo beer";
            String pPhoneNumber2 = "9121231234";
            Patient patient2 = Patient.builder()
                    .name(pName2)
                    .phoneNumber(pPhoneNumber2)
                    .build();
            List<Appointment> appointments = getTwoAppointments();
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
            patientRepository.saveAll(List.of(patient1, patient2));
            appointments.get(0).setPatient(patient1);
            appointments.get(1).setPatient(patient1);
            appointmentRepository.saveAll(appointments);

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(pageable, null);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(0);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(2);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD));
        }

        @Test
        @DisplayName("with null patient and pageable, should return valid-empty page")
        void testFindAllByPatientWithNullPatientAndPageable() {

            Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(null, null);

            assertThat(appointmentPage).isNotNull();
            assertThat(appointmentPage.getTotalElements()).isEqualTo(0);
            assertThat(appointmentPage.getTotalPages()).isEqualTo(1);
            assertThat(appointmentPage.getNumberOfElements()).isEqualTo(0);
            assertThat(appointmentPage.getSize()).isEqualTo(0);
            assertThat(appointmentPage.getPageable()).isNotNull();
            assertThat(appointmentPage.getSort()).isEqualTo(Sort.unsorted());
        }

    }

}
