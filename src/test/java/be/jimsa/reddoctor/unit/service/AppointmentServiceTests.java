package be.jimsa.reddoctor.unit.service;


import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_DEFAULT_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentUtils appointmentUtils;

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
        end = LocalTime.of(12, 17, 27);
    }

    @Nested
    @DisplayName("CreateAppointments")
    class CreateAppointmentsTests {

        @Test
        @DisplayName("by a valid dto, should return a list of dtos")
        void givenAValidAppointmentDto_whenCreateAppointments_thenReturnAValidListOfDtos() {
            // given (Arrange) - precondition or setup:
            AppointmentDto appointmentDto = AppointmentDto.builder()
                    .date(date)
                    .start(start)
                    .end(end)
                    .build();
            Appointment appointmentEntity = Appointment.builder()
                    .id(1L)
                    .patient(null)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .build();
            List<Appointment> appointmentList = new ArrayList<>();
            appointmentList.add(appointmentEntity);
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(Optional.of(new ArrayList<>()));
            given(appointmentRepository.saveAll(anyList())).willReturn(appointmentList);
            given(appointmentRepository.save(any())).willReturn(appointmentEntity);

            // when (Act) - action or the behavior that we are going test:
            List<AppointmentDto> appointmentDtos = appointmentService.createAppointments(appointmentDto);

            // then(Assert) - verify the output:
            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(1)
                    .isInstanceOf(List.class);

            appointmentDto.setDate(appointmentDto.getDate().plusDays(1));
            assertTimeout(Duration.ofMillis(10), () -> appointmentService.createAppointments(appointmentDto));

        }

    }


}
