package be.jimsa.reddoctor.unit.controller;


import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.controller.DoctorController;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.DATE_FORMAT;
import static be.jimsa.reddoctor.utility.constant.ProjectConstants.PUBLIC_ID_DEFAULT_LENGTH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Spy
    private PublicIdGenerator publicIdGenerator;

    private String projectUrl;
    private String publicId;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    @BeforeEach
    void setUp() {
        projectUrl = "/v0.9/doctors";
        publicId = publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH);
    }

    @Nested
    @DisplayName("AddAppointments")
    class AddAppointmentsTests {

        @Test
        @DisplayName("by a valid dto, should return a valid response dto")
        void givenAValidAppointmentDto_whenAddAppointments_thenReturnAValidResponseDto() throws Exception {
        	// given (Arrange) - precondition or setup:
            date = LocalDate.parse("09-10-2024", DateTimeFormatter.ofPattern(DATE_FORMAT));
            start = LocalTime.of(10, 15, 20);
            end = LocalTime.of(12, 17, 27);
            AppointmentDto appointmentDto = AppointmentDto.builder()
                    .date(date)
                    .start(start)
                    .end(end)
                    .build();
            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            appointmentDtos.add(appointmentDto);
            given(appointmentService.createAppointments(any())).willReturn(appointmentDtos);

        	// when (Act) - action or the behavior that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(appointmentDto))
            );

        	// then(Assert) - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.action").value(true))
                    .andExpect(jsonPath("$.result[0].public_id").isEmpty());
//                    .andExpect(jsonPath("$.result[0].date", is(date.toString())));

        }
    }


}
