package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.enums.Status;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_EXAMPLE_1;
import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_EXAMPLE_2;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;


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


    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("by a valid entity, should return a valid saved entity with id")
        void givenAValidAppointmentEntity_whenSave_thenReturnSavedAppointmentEntityWithId() {
            Appointment appointment = Appointment.builder()
//                    .publicId(publicId)
//                    .date(date)
//                    .startTime(start)
//                    .endTime(end)
                    .build();

            Appointment savedAppointment = appointmentRepository.save(appointment);

            assertThat(savedAppointment)
                    .isNotNull()
                    .isInstanceOf(Appointment.class)
                    .hasFieldOrPropertyWithValue("publicId", savedAppointment.getPublicId())
                    .hasFieldOrPropertyWithValue("date", savedAppointment.getDate())
                    .hasFieldOrPropertyWithValue("startTime", savedAppointment.getStartTime())
                    .hasFieldOrPropertyWithValue("endTime", savedAppointment.getEndTime())
                    .hasFieldOrPropertyWithValue("patient", null);
            assertThat(savedAppointment.getId())
                    .isNotNull()
                    .isPositive();
            assertThat(appointmentRepository.count()).isEqualTo(1);
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
