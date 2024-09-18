package be.jimsa.reddoctor.unit.utility;


import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

class AppointmentUtilsTests {

    private PublicIdGenerator publicIdGenerator;
    private AppointmentUtils appointmentUtils;

    @BeforeEach
    void setUp() {
        publicIdGenerator = new PublicIdGenerator();
        appointmentUtils = new AppointmentUtils(publicIdGenerator);
    }

    @Nested
    @DisplayName("MapToEntity")
    class MapToEntityTests {
        
    }

    @Nested
    @DisplayName("MapToDto")
    class MapToDtoTests {

    }

    @Nested
    @DisplayName("Splitter")
    class SplitterTests {

    }

    @Nested
    @DisplayName("PrintLogger")
    class PrintLoggerTests {

    }

}
