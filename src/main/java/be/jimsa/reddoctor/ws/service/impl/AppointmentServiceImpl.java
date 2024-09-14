package be.jimsa.reddoctor.ws.service.impl;


import be.jimsa.reddoctor.config.exception.BadFormatRequestException;
import be.jimsa.reddoctor.config.exception.NotFoundResourceException;
import be.jimsa.reddoctor.config.exception.ReservedResourceException;
import be.jimsa.reddoctor.config.log.EvaluateExecuteTimeout;
import be.jimsa.reddoctor.utility.id.PublicIdGenerator;
import be.jimsa.reddoctor.utility.mapper.AppointmentMapper;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PublicIdGenerator publicIdGenerator;

    @EvaluateExecuteTimeout
    @Override
    public List<AppointmentDto> createAppointments(AppointmentDto appointmentDto) {

        // Logic 00: date check?!
        Optional<List<Appointment>> optionalList = appointmentRepository.findAllByDate(appointmentDto.getDate());
        if (optionalList.isPresent() && !optionalList.get().isEmpty()) {
            throw new BadFormatRequestException(EXCEPTION_DATE_MESSAGE);
        }

        // Logic 01: end - start >= 0
        int l1 = appointmentDto.getEnd().compareTo(appointmentDto.getStart());
        if (l1 < 0) {
            throw new BadFormatRequestException(EXCEPTION_START_END_FORMAT_MESSAGE);
        }

        // Logic 02: |end - start| < 30 min ==> ignore
        List<AppointmentDto> appointmentDtos = splitter(appointmentDto);

        List<Appointment> savedAppointments = appointmentRepository.saveAll(
                appointmentDtos.stream()
                        .map(dto -> appointmentRepository.save(appointmentMapper.mapToEntity(dto)))
                        .toList()
        );
        return savedAppointments.stream()
                .map(appointmentMapper::mapToDto)
                .toList();
    }

    private List<AppointmentDto> splitter(AppointmentDto appointmentDto) {
        Duration interval = Duration.ofMinutes(30);
        LocalTime current = appointmentDto.getStart();
        List<AppointmentDto> dtos = new ArrayList<>();

        while (current.plus(interval).isBefore(appointmentDto.getEnd()) || current.plus(interval).equals(appointmentDto.getEnd())) {
            AppointmentDto dto = AppointmentDto.builder()
                    .start(current)
                    .end(current.plus(interval))
                    .date(appointmentDto.getDate())
                    .publicId(publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH))
                    .type(GENERAL_TYPE_OPEN)
                    .build();
            current = current.plus(interval);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<AppointmentDto> readAppointments(String dateStr, int page, int size, String type, String sortDirection) {

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));

        Sort sort;
        if (sortDirection.equalsIgnoreCase(GENERAL_SORT_DIRECTION_ASC_FIELD)) {
            sort = Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD);
        } else {
            sort = Sort.by(Sort.Direction.DESC, APPOINTMENT_TIME_FIELD);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                sort
        );

        Page<Appointment> appointmentPage;
        if (type.equalsIgnoreCase(GENERAL_TYPE_ALL)) {
            appointmentPage = appointmentRepository.findAllByDate(pageable, date);
        } else {
            appointmentPage = appointmentRepository.findAllByPatientIsNullAndDate(pageable, date);
        }

        printLogger(appointmentPage);

        return appointmentPage
                .get()
                .map(appointmentMapper::mapToDto)
                .toList();
    }

    private void printLogger(Page<Appointment> appointmentPage) {
        log.info(LOGGER_TOTAL_ELEMENTS, appointmentPage.getTotalElements());
        log.info(LOGGER_TOTAL_PAGES, appointmentPage.getTotalPages());
        log.info(LOGGER_NUMBER_OF_ELEMENTS, appointmentPage.getNumberOfElements());
        log.info(LOGGER_SIZE, appointmentPage.getSize());
    }

    @Override
    public boolean removeAnAppointment(String publicId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(publicId);
        if (optionalAppointment.isPresent()) {
            if (optionalAppointment.get().getPatient() == null) {
                appointmentRepository.delete(optionalAppointment.get());
                return true;
            } else {
                throw new ReservedResourceException(EXCEPTION_NOT_ACCEPTABLE_RESOURCE_MESSAGE);
            }
        } else {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }
    }
}
