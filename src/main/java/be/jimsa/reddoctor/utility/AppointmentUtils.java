package be.jimsa.reddoctor.utility;

import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Component
@AllArgsConstructor
public class AppointmentUtils {

    private final PublicIdGenerator publicIdGenerator;

    public Appointment mapToEntity(AppointmentDto dto) {
        Patient patient = dto.getPatientDto() == null ? null : Patient.builder()
                .name(dto.getPatientDto().getName())
                .phoneNumber(dto.getPatientDto().getPhoneNumber())
                .build();
        return Appointment.builder()
                .date(dto.getDate())
                .startTime(dto.getStart())
                .endTime(dto.getEnd())
                .publicId(dto.getPublicId())
                .patient(patient)
                .status(dto.getStatus())
                .build();
    }

    public AppointmentDto mapToDto(Appointment entity) {
        PatientDto patientDto = entity.getPatient() == null ? null : PatientDto.builder()
                .name(entity.getPatient().getName())
                .phoneNumber(entity.getPatient().getPhoneNumber())
                .build();
        return AppointmentDto.builder()
                .date(entity.getDate())
                .start(entity.getStartTime())
                .end(entity.getEndTime())
                .publicId(entity.getPublicId())
                .patientDto(patientDto)
                .status(entity.getStatus())
                .build();
    }

    public List<AppointmentDto> splitter(AppointmentDto appointmentDto) {
        Duration interval = Duration.ofMinutes(30);
        LocalTime current = appointmentDto.getStart();
        List<AppointmentDto> dtos = new ArrayList<>();
        while (current.plus(interval).isBefore(appointmentDto.getEnd()) || current.plus(interval).equals(appointmentDto.getEnd())) {
            AppointmentDto dto = AppointmentDto.builder()
                    .start(current)
                    .end(current.plus(interval))
                    .date(appointmentDto.getDate())
                    .publicId(publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH))
                    .status(Status.OPEN)
                    .build();
            current = current.plus(interval);
            dtos.add(dto);
        }
        return dtos;
    }
}
