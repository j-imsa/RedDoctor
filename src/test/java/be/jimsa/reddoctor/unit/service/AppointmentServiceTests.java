package be.jimsa.reddoctor.unit.service;


import be.jimsa.reddoctor.config.exception.AppServiceException;
import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PublicIdGenerator publicIdGenerator;

    @Mock
    private AppointmentUtils appointmentUtils;

    @BeforeEach
    void setup() {

    }

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

    @Nested
    @DisplayName("CreateAppointments")
    class CreateAppointmentsTests {

        @Test
        @DisplayName("with valid dto, should return valid list")
        void testCreateAppointmentsWithValidDto() {
            // AppointmentDto
            AppointmentDto appointmentDto = generateAppointmentDto();
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(Collections.emptyList());
            // splitter
            given(appointmentUtils.splitter(any(AppointmentDto.class))).willReturn(generateAppointmentDtos());
            // saveAll
            given(appointmentRepository.saveAll(anyList())).willReturn(generateAppointments());
            // mapToEntity
            given(appointmentUtils.mapToEntity(any(AppointmentDto.class))).willReturn(generateAppointment());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class))).willReturn(generateAppointmentDto());
            // expected
            int expectedSize = 2;

            List<AppointmentDto> savedAppointmentDtos = appointmentService.createAppointments(appointmentDto);

            assertThat(savedAppointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
            assertThat(savedAppointmentDtos.get(0)).isEqualTo(generateAppointmentDto());
            verify(appointmentRepository, times(1)).findAllByDate(any(LocalDate.class));
            verify(appointmentUtils, times(1)).splitter(any(AppointmentDto.class));
            verify(appointmentRepository, times(1)).saveAll(anyList());
            verify(appointmentUtils, times(2)).mapToEntity(any(AppointmentDto.class));
            verify(appointmentUtils, times(2)).mapToDto(any(Appointment.class));
        }

        @Test
        @DisplayName("with invalid date, should throw AppServiceException")
        void testCreateAppointmentsWithInvalidDate() {
            // AppointmentDto
            AppointmentDto appointmentDto = generateAppointmentDto();
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(generateAppointments());

            assertThatThrownBy(() -> appointmentService.createAppointments(appointmentDto))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_DATE_MESSAGE);
            verify(appointmentRepository, times(1)).findAllByDate(any(LocalDate.class));
            verify(appointmentUtils, times(0)).splitter(any(AppointmentDto.class));
            verify(appointmentRepository, times(0)).saveAll(anyList());
            verify(appointmentUtils, times(0)).mapToEntity(any(AppointmentDto.class));
            verify(appointmentUtils, times(0)).mapToDto(any(Appointment.class));
        }

        @Test
        @DisplayName("with invalid time(short than general interval), should return valid-empty list")
        void testCreateAppointmentsWithInvalidTime() {
            // AppointmentDto
            AppointmentDto appointmentDto = generateAppointmentDto();
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(Collections.emptyList());
            // splitter
            given(appointmentUtils.splitter(any(AppointmentDto.class))).willReturn(Collections.emptyList());
            // saveAll
            given(appointmentRepository.saveAll(anyList())).willReturn(Collections.emptyList());

            List<AppointmentDto> savedAppointmentDtos = appointmentService.createAppointments(appointmentDto);

            assertThat(savedAppointmentDtos)
                    .isNotNull()
                    .isEmpty();
            verify(appointmentRepository, times(1)).findAllByDate(any(LocalDate.class));
            verify(appointmentUtils, times(1)).splitter(any(AppointmentDto.class));
            verify(appointmentRepository, times(1)).saveAll(anyList());
            verify(appointmentUtils, times(0)).mapToEntity(any(AppointmentDto.class));
            verify(appointmentUtils, times(0)).mapToDto(any(Appointment.class));
        }

        @Test
        @DisplayName("with null dto, should throw NullPointerException")
        void testCreateAppointmentsWithNullDto() {
            assertThatThrownBy(() -> appointmentService.createAppointments(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
            verify(appointmentRepository, times(0)).findAllByDate(any(LocalDate.class));
            verify(appointmentUtils, times(0)).splitter(any(AppointmentDto.class));
            verify(appointmentRepository, times(0)).saveAll(anyList());
            verify(appointmentUtils, times(0)).mapToEntity(any(AppointmentDto.class));
            verify(appointmentUtils, times(0)).mapToDto(any(Appointment.class));
        }

        @Test
        @DisplayName("with save all fails, should throw RuntimeException")
        void testCreateAppointmentsWithSaveAllFails() {
            // AppointmentDto
            AppointmentDto appointmentDto = generateAppointmentDto();
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(Collections.emptyList());
            // splitter
            given(appointmentUtils.splitter(any(AppointmentDto.class))).willReturn(generateAppointmentDtos());
            // saveAll
            given(appointmentRepository.saveAll(anyList())).willThrow(new RuntimeException("db fails"));
            // mapToEntity
            given(appointmentUtils.mapToEntity(any(AppointmentDto.class))).willReturn(generateAppointment());

            assertThatThrownBy(() -> appointmentService.createAppointments(appointmentDto))
                    .isInstanceOf(RuntimeException.class);
            verify(appointmentRepository, times(1)).findAllByDate(any(LocalDate.class));
            verify(appointmentUtils, times(1)).splitter(any(AppointmentDto.class));
            verify(appointmentRepository, times(1)).saveAll(anyList());
            verify(appointmentUtils, times(2)).mapToEntity(any(AppointmentDto.class));
            verify(appointmentUtils, times(0)).mapToDto(any(Appointment.class));
            assertThat(appointmentRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("transactional?, should rollback")
        void testCreateAppointmentsTransactional() {
            // AppointmentDto
            AppointmentDto appointmentDto = generateAppointmentDto();
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(LocalDate.class))).willReturn(Collections.emptyList());
            // splitter
            given(appointmentUtils.splitter(any(AppointmentDto.class))).willReturn(generateAppointmentDtos());
            // saveAll
            given(appointmentRepository.saveAll(anyList())).willReturn(generateAppointments());
            // mapToEntity
            given(appointmentUtils.mapToEntity(any(AppointmentDto.class))).willReturn(generateAppointment());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class))).willThrow(new RuntimeException("transactional")); // exception, after saveAll
            // expected
            int expectedSize = 0;

            assertThatThrownBy(() -> appointmentService.createAppointments(appointmentDto))
                    .isInstanceOf(RuntimeException.class);
            assertThat(appointmentRepository.count()).isEqualTo(expectedSize);
        }

    }

    @Nested
    @DisplayName("ReadAppointments")
    class ReadAppointmentsTests {

    }

    @Nested
    @DisplayName("RemoveAnAppointment")
    class removeAnAppointmentTests {

    }


}
