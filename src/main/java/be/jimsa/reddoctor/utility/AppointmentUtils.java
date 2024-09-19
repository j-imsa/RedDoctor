package be.jimsa.reddoctor.utility;

import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    /**
     * @param appointmentDto the appointment data transfer object
     * @return a list of valid appointmentDTOs
     * @apiNote Constraints:
     * <ol>
     *   <li>Start cannot be 23:59:59 (MAX).</li>
     *   <li>End cannot be 00:00:00 (Min).</li>
     *   <li>|end - start| > 1s</li>
     * </ol>
     *
     * <p>
     * Time Sequence Representation:
     * <pre>
     *  |         |         |         |
     * min      start      end       max
     *  |         |         |         |
     * </pre>
     * </p>
     * @implNote 4 Validation Stages:
     * <ol>
     *   <li>min == start && end == max</li>
     *   <li>min < start && end < max</li>
     *   <li>min == start && end < max</li>
     *   <li>min < start && end == max</li>
     * </ol>
     */
    public List<AppointmentDto> splitter(AppointmentDto appointmentDto) {
        Duration interval = Duration.ofMinutes(GENERAL_DURATION);
        LocalTime start = appointmentDto.getStart();
        LocalTime end = appointmentDto.getEnd();
        LocalTime max = LocalTime.MAX.truncatedTo(ChronoUnit.SECONDS);
        LocalTime min = LocalTime.MIN.truncatedTo(ChronoUnit.SECONDS);
        LocalTime index = LocalTime.of(23, 30, 0);

        List<AppointmentDto> dtos = new ArrayList<>();

        Duration durationBetweenStartAndMax = Duration.between(start, max);
        Duration durationBetweenMinAndEnd = Duration.between(min, end);
        if (durationBetweenStartAndMax.isZero() || durationBetweenMinAndEnd.isZero()) {
            return dtos;
        }

        Duration durationBetweenStartAndEnd = Duration.between(start, end);
        if (durationBetweenStartAndEnd.isNegative() || durationBetweenStartAndEnd.isZero()) { // start > end or start = end
            return dtos;
        } else { // durationBetweenStartAndEnd.isPositive()
            LocalTime tmp;
            while (true) {
                if (Duration.between(start, index).isNegative() || Duration.between(start, index).isZero()) {
                    break;
                } else {
                    tmp = start.plus(interval);
                }
                if (Duration.between(tmp, end).isNegative()) {
                    break;
                } else if (Duration.between(tmp, end).isZero()) {
                    dtos.add(generateAddableAppointmentDto(appointmentDto.getDate(), start, tmp));
                    break;
                } else {
                    dtos.add(generateAddableAppointmentDto(appointmentDto.getDate(), start, tmp));
                    start = tmp;
                }
            }
        }
        return dtos;
    }

    private AppointmentDto generateAddableAppointmentDto(LocalDate date, LocalTime start, LocalTime end) {
        return AppointmentDto.builder()
                .publicId(publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH))
                .date(date)
                .start(start)
                .end(end)
                .status(Status.OPEN)
                .build();
    }

    public void printLogger(Logger logger, Page<Appointment> appointmentPage) {
        if (logger != null && appointmentPage != null) {
            logger.info(LOGGER_TOTAL_ELEMENTS, appointmentPage.getTotalElements());
            logger.info(LOGGER_TOTAL_PAGES, appointmentPage.getTotalPages());
            logger.info(LOGGER_NUMBER_OF_ELEMENTS, appointmentPage.getNumberOfElements());
            logger.info(LOGGER_SIZE, appointmentPage.getSize());
        }
    }

}
