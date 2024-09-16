package be.jimsa.reddoctor.unit.repository;


import be.jimsa.reddoctor.ws.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_EXAMPLE_1;

@DataJpaTest
class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;


    private String publicId;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    @BeforeEach
    void setup() {
        publicId = PUBLIC_ID_EXAMPLE_1;
        date = LocalDate.of(2024, 9, 10);
        start = LocalTime.of(10, 15, 20);
        end = LocalTime.of(12, 17, 22);
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

    }

    @Nested
    @DisplayName("FindByPhoneNumber")
    class FindByPhoneNumberTests {

    }

}
