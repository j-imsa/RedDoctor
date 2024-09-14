package be.jimsa.reddoctor.utility.mapper;

import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import org.springframework.stereotype.Component;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.GENERAL_TYPE_ALL;
import static be.jimsa.reddoctor.utility.constant.ProjectConstants.GENERAL_TYPE_OPEN;

@Component
public class AppointmentMapper {

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
                .type(
                        patientDto == null ? GENERAL_TYPE_OPEN : GENERAL_TYPE_ALL
                )
                .build();
    }
}
