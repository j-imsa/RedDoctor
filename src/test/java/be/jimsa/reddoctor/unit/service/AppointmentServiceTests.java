package be.jimsa.reddoctor.unit.service;


import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_DEFAULT_LENGTH;

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
    }

    @Nested
    @DisplayName("CreateAppointments")
    class CreateAppointmentsTests {

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
