package be.jimsa.reddoctor.ws.service;


import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> createAppointments(AppointmentDto appointmentDto);

    List<AppointmentDto> readAppointments(String date, int page, int size, String statusStr, String sortDirection);

    boolean removeAnAppointment(String publicId);
}
