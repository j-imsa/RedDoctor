package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_DEFAULT_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // @PersistenceContext private EntityManager entityManager;

    @Spy
    private PublicIdGenerator publicIdGenerator;

    private String publicId;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    @BeforeEach
    void cleanDatabase() {
        publicId = publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH);
        date = LocalDate.of(2024, 9, 10);
        start = LocalTime.of(10, 15, 20);
        end = LocalTime.of(12, 17, 22);
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("by a valid entity, should return a valid saved entity with id")
        void givenAValidAppointmentEntity_whenSave_thenReturnSavedAppointmentEntityWithId() {
            Appointment appointment = Appointment.builder()
                    .publicId(publicId)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
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


}
