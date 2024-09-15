package be.jimsa.reddoctor.ws.service;

import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;

import java.util.List;

public interface PatientService {
    AppointmentDto updateAnAppointment(String publicId, PatientDto patientDto);

    List<AppointmentDto> readMyAppointments(String phoneNumber, int page, int size, String sortDirection);
}
