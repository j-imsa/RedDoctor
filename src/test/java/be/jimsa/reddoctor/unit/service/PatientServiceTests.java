package be.jimsa.reddoctor.unit.service;

import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.mapper.PatientMapper;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.repository.PatientRepository;
import be.jimsa.reddoctor.ws.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTests {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Spy
    private PatientMapper patientMapper;

    @Spy
    private AppointmentUtils appointmentUtils;

    @Nested
    @DisplayName("UpdateAnAppointment")
    class UpdateAnAppointmentTests {

    }

    @Nested
    @DisplayName("ReadMyAppointments")
    class ReadMyAppointmentsTests {

    }

}
