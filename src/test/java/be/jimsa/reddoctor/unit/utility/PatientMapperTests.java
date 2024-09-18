package be.jimsa.reddoctor.unit.utility;

import be.jimsa.reddoctor.utility.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

public class PatientMapperTests {

    private PatientMapper patientMapper;

    @BeforeEach
    public void setUp() {
        patientMapper = new PatientMapper();
    }

    @Nested
    @DisplayName("MapToEntity")
    class MapToEntityTests {

    }

    @Nested
    @DisplayName("MapToDto")
    class MapToDtoTests {

    }

}
