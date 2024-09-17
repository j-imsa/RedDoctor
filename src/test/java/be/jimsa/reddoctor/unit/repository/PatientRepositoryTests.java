package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;



    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("with valid patient, should return saved patient with id")
        void testSaveWithValidInfo() {
        	// given (Arrange) - precondition or setup:
            String patientName = "Foo bar";
            String patientPhoneNumber = "9131231234";
            Patient patient = Patient.builder()
                    .name(patientName)
                    .phoneNumber(patientPhoneNumber)
                    .build();

        	// when (Act) - action or the behavior that we are going test:
            Patient savedPatient = patientRepository.save(patient);

        	// then(Assert) - verify the output:
            assertThat(savedPatient)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", savedPatient.getId())
                    .hasFieldOrPropertyWithValue("name", patientName)
                    .hasFieldOrPropertyWithValue("phoneNumber", patientPhoneNumber);
        }

        @Test
        @DisplayName("with null patient, should throw InvalidDataAccessApiUsageException")
        void testSaveWithNull() {
            assertThatThrownBy(() -> patientRepository.save(null))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class)
                            .hasMessageContaining("Entity must not be null");
        }

        @Test
        @DisplayName("with null name, should throw DataIntegrityViolationException")
        void testSaveWithNullName() {
            // given (Arrange) - precondition or setup:
            String patientName = "Foo bar";
            String patientPhoneNumber = "9131231234";
            Patient patient = Patient.builder()
                    .name(null)
                    .phoneNumber(patientPhoneNumber)
                    .build();

            assertThatThrownBy(()-> patientRepository.save(patient))
                    .isInstanceOf(DataIntegrityViolationException.class)
                            .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with null phone_number, should throw DataIntegrityViolationException")
        void testSaveWithNullPhoneNumber() {
            // given (Arrange) - precondition or setup:
            String patientName = "Foo bar";
            String patientPhoneNumber = "9131231234";
            Patient patient = Patient.builder()
                    .name(patientName)
                    .phoneNumber(null)
                    .build();

            assertThatThrownBy(()-> patientRepository.save(patient))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

        @Test
        @DisplayName("with duplicate phone_number, should throw DataIntegrityViolationException")
        void testSaveWithDuplicatePhoneNumber() {
            // given (Arrange) - precondition or setup:
            String patientName1 = "Foo bar";
            String patientName2 = "Foo beer";
            String patientPhoneNumber = "9131231234";
            Patient patient1 = Patient.builder()
                    .name(patientName1)
                    .phoneNumber(patientPhoneNumber)
                    .build();
            Patient patient2 = Patient.builder()
                    .name(patientName2)
                    .phoneNumber(patientPhoneNumber)
                    .build();
            patientRepository.save(patient1);

            assertThatThrownBy(()-> patientRepository.save(patient2))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");
        }

    }

    @Nested
    @DisplayName("FindByPhoneNumber")
    class FindByPhoneNumberTests {

    }

}
