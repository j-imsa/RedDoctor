package be.jimsa.reddoctor.utility.mapper;

import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient mapToEntity(PatientDto patientDto) {
        return Patient.builder()
                .name(patientDto.getName())
                .phoneNumber(patientDto.getPhoneNumber())
                .build();
    }

    public Patient mapToEntityById(PatientDto patientDto, long id) {
        return Patient.builder()
                .id(id)
                .name(patientDto.getName())
                .phoneNumber(patientDto.getPhoneNumber())
                .build();
    }
}
