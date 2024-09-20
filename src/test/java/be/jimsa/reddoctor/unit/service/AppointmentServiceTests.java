package be.jimsa.reddoctor.unit.service;


import be.jimsa.reddoctor.config.exception.AppServiceException;
import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.enums.Status;
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
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    private Page<Appointment> generateAppointmentPage(int page, int size, int total) {
        return new PageImpl<>(
                generateAppointments(),
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD)),
                total
        );
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

        @Test
        @DisplayName("with valid data (all), should return valid list")
        void testReadAppointmentsWithValidDataAll() {
            String dateStr = "16-01-2020";
            int page = 1;
            int size = 20;
            String statusStr = "all"; // invalid status
            String sortDirection = "desc";
            // findAllByDate
            given(appointmentRepository.findAllByDate(any(), any(LocalDate.class))).willReturn(generateAppointmentPage(page, size, 2 * size));
            // printLogger
            doNothing().when(appointmentUtils).printLogger(any(Logger.class), any());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class))).willReturn(generateAppointmentDto());
            // expected
            int expectedSize = 2;

            List<AppointmentDto> appointmentDtos = appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
        }

        @Test
        @DisplayName("with valid data (open), should return valid list")
        void testReadAppointmentsWithValidDataOpen() {
            String dateStr = "16-01-2020";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "desc";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDateAndStatus(any(), any(LocalDate.class), any())).willReturn(generateAppointmentPage(page, size, 2 * size));
            // printLogger
            doNothing().when(appointmentUtils).printLogger(any(Logger.class), any());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class))).willReturn(generateAppointmentDto());
            // expected
            int expectedSize = 2;

            List<AppointmentDto> appointmentDtos = appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize);
        }

        @Test
        @DisplayName("with invalid time, should throw DateTimeParseException")
        void testReadAppointmentsWithInvalidTime() {
            String dateStr = "2020- 16-01";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "desc";

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(DateTimeParseException.class)
                    .hasMessageContaining("could not be parsed");
        }

        @Test
        @DisplayName("with null time, should throw NullPointerException")
        void testReadAppointmentsWithNullTime() {
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "desc";

            assertThatThrownBy(() -> appointmentService.readAppointments(null, page, size, statusStr, sortDirection))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("text");
        }

        @Test
        @DisplayName("with invalid page, should throw IllegalArgumentException")
        void testReadAppointmentsWithInvalidPage() {
            String dateStr = "16-01-2020";
            int page = -10;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "desc";

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Page index must not be less than zero");
        }

        @Test
        @DisplayName("with invalid size, should throw IllegalArgumentException")
        void testReadAppointmentsWithInvalidSize() {
            String dateStr = "16-01-2020";
            int page = 1;
            int size = -20;
            String statusStr = "open";
            String sortDirection = "desc";

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Page size must not be less than one");
        }

        @Test
        @DisplayName("with invalid sort direction, should return reversed-sorted list")
        void testReadAppointmentsWithInvalidSortDirection() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "xyz";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDateAndStatus(any(), any(LocalDate.class), any())).willReturn(generateAppointmentPage(page, size, 2 * size));
            // printLogger
            doNothing().when(appointmentUtils).printLogger(any(Logger.class), any());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class)))
                    // DESC order, return
                    .willReturn(generateAppointmentDtos().get(1))
                    .willReturn(generateAppointmentDtos().get(0));
            // expected
            int expectedSize = 2;

            List<AppointmentDto> appointmentDtos = appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection);

            assertThat(appointmentDtos)
                    .isNotNull()
                    .hasSize(expectedSize)
                    .isSortedAccordingTo(Comparator.comparing(AppointmentDto::getStart).reversed());
        }

        @Test
        @DisplayName("with null sort direction, should throw NullPointerException")
        void testReadAppointmentsWithNullSortDirection() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "open";

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
        }

        @Test
        @DisplayName("with db exception, by status, should throw RuntimeException")
        void testReadAppointmentsWithDbExceptionWithStatus() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "asc";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDateAndStatus(any(), any(LocalDate.class), any()))
                    .willThrow(new RuntimeException("db"));

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("db");
        }

        @Test
        @DisplayName("with db exception, without status, should throw RuntimeException")
        void testReadAppointmentsWithDbExceptionWithoutStatus() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "all";
            String sortDirection = "asc";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDate(any(), any(LocalDate.class)))
                    .willThrow(new RuntimeException("db"));

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("db");
        }

        @Test
        @DisplayName("with utils exception, printLogger, should throw RuntimeException")
        void testReadAppointmentsWithUtilsExceptionPrintLogger() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "asc";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDateAndStatus(any(), any(LocalDate.class), any()))
                    .willReturn(generateAppointmentPage(page, size, 2 * size));
            // printLogger
            doThrow(new RuntimeException("printLogger"))
                    .when(appointmentUtils)
                    .printLogger(any(Logger.class), any());

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("printLogger");
        }

        @Test
        @DisplayName("with utils exception, mapToDto, should throw RuntimeException")
        void testReadAppointmentsWithUtilsExceptionMapToDto() {
            String dateStr = "20-09-2024";
            int page = 1;
            int size = 20;
            String statusStr = "open";
            String sortDirection = "asc";
            // findAllByDateAndStatus
            given(appointmentRepository.findAllByDateAndStatus(any(), any(LocalDate.class), any()))
                    .willReturn(generateAppointmentPage(page, size, 2 * size));
            // printLogger
            doNothing().when(appointmentUtils).printLogger(any(Logger.class), any());
            // mapToDto
            given(appointmentUtils.mapToDto(any(Appointment.class)))
                    .willThrow(new RuntimeException("mapToDto"));

            assertThatThrownBy(() -> appointmentService.readAppointments(dateStr, page, size, statusStr, sortDirection))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("mapToDto");
        }

    }

    @Nested
    @DisplayName("RemoveAnAppointment")
    class RemoveAnAppointmentTests {

        @Test
        @DisplayName("with valid public_id, should return true")
        void testRemoveAnAppointmentWithValidPublicId() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));
            given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment);

            boolean result = appointmentService.removeAnAppointment(PUBLIC_ID_EXAMPLE_1);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("with invalid public_id, should throw AppServiceException")
        void testRemoveAnAppointmentWithInvalidPublicId() {
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.removeAnAppointment(PUBLIC_ID_EXAMPLE_1))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }

        @Test
        @DisplayName("with null public_id, should throw AppServiceException")
        void testRemoveAnAppointmentWithNullPublicId() {
            given(appointmentRepository.findByPublicId(any())).willReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.removeAnAppointment(null))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }

        @Test
        @DisplayName("with deleted status, should throw AppServiceException")
        void testRemoveAnAppointmentWithDeletedStatus() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.DELETED);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));

            assertThatThrownBy(() -> appointmentService.removeAnAppointment(PUBLIC_ID_EXAMPLE_1))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_RESOURCE_ALREADY_DELETED_MESSAGE);
        }

        @Test
        @DisplayName("with taken status, should throw AppServiceException")
        void testRemoveAnAppointmentWithTakenStatus() {
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.TAKEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));

            assertThatThrownBy(() -> appointmentService.removeAnAppointment(PUBLIC_ID_EXAMPLE_1))
                    .isInstanceOf(AppServiceException.class)
                    .hasMessageContaining(EXCEPTION_NOT_ACCEPTABLE_RESOURCE_MESSAGE);
        }

        @Test
        @DisplayName("transactional? should rollback")
        void testRemoveAnAppointmentWithTransactional() {
            long initialCount = appointmentRepository.count();
            Appointment appointment = generateAppointment();
            appointment.setStatus(Status.OPEN);
            given(appointmentRepository.findByPublicId(anyString())).willReturn(Optional.of(appointment));
            doAnswer(invocation -> {
                Appointment savedAppointment = invocation.getArgument(0);
                throw new RuntimeException("transactional");
            }).when(appointmentRepository).save(any(Appointment.class));

            assertThatThrownBy(() -> appointmentService.removeAnAppointment(PUBLIC_ID_EXAMPLE_1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("transactional");
            assertThat(appointmentRepository.count()).isEqualTo(initialCount);
        }

    }

}
