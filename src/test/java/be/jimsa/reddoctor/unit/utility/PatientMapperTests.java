package be.jimsa.reddoctor.unit.utility;

import be.jimsa.reddoctor.utility.mapper.PatientMapper;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PatientMapperTests {

    private PatientMapper patientMapper;

    @BeforeEach
    public void setUp() {
        patientMapper = new PatientMapper();
    }

    private Patient getPatient() {
        return Patient.builder()
                .id(100L)
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }

    private PatientDto getPatientDto() {
        return PatientDto.builder()
                .name("Foo bar")
                .phoneNumber("9131231234")
                .build();
    }

    @Nested
    @DisplayName("MapToEntity")
    class MapToEntityTests {

        @Test
        @DisplayName("with valid dto, should return valid entity")
        void testMapToEntityWithValidDto() {
            PatientDto patientDto = getPatientDto();

            Patient patient = patientMapper.mapToEntity(patientDto);

            assertThat(patient).isNotNull();
            assertThat(patient.getId()).isNull();
            assertThat(patient.getName()).isNotNull().isEqualTo(patientDto.getName());
            assertThat(patient.getPhoneNumber()).isNotNull().isEqualTo(patientDto.getPhoneNumber());
        }

        @Test
        @DisplayName("with null dto, should throw ")
        void testMapToEntityWithNullDto() {
            assertThatThrownBy(() -> patientMapper.mapToEntity(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke");
        }

        @Test
        @DisplayName("with valid dto and invalid values, should return valid-same entity")
        void testMapToEntityWithValidDtoAndInvalidValues() {
            PatientDto patientDto = getPatientDto();
            patientDto.setName("I");
            patientDto.setPhoneNumber("Invalid phone number!!!");

            Patient patient = patientMapper.mapToEntity(patientDto);

            assertThat(patient).isNotNull();
            assertThat(patient.getId()).isNull();
            assertThat(patient.getName()).isNotNull().isEqualTo(patientDto.getName());
            assertThat(patient.getPhoneNumber()).isNotNull().isEqualTo(patientDto.getPhoneNumber());
        }

        @Test
        @DisplayName("with valid dto and null values, should return valid-same entity")
        void testMapToEntityWithValidDtoAndNullValues() {
            PatientDto patientDto = getPatientDto();
            patientDto.setName(null);
            patientDto.setPhoneNumber(null);

            Patient patient = patientMapper.mapToEntity(patientDto);

            assertThat(patient).isNotNull();
            assertThat(patient.getId()).isNull();
            assertThat(patient.getName()).isNull();
            assertThat(patient.getPhoneNumber()).isNull();
        }

    }

    @Nested
    @DisplayName("MapToDto")
    class MapToDtoTests {

    }

}
