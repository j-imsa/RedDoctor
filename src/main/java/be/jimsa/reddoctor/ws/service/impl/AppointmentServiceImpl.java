package be.jimsa.reddoctor.ws.service.impl;


import be.jimsa.reddoctor.config.exception.AppServiceException;
import be.jimsa.reddoctor.config.log.EvaluateExecuteTimeout;
import be.jimsa.reddoctor.utility.AppointmentUtils;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.enums.Status;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentUtils appointmentUtils;

    @EvaluateExecuteTimeout
    @Override
    public List<AppointmentDto> createAppointments(AppointmentDto appointmentDto) {

        Optional<List<Appointment>> optionalList = appointmentRepository.findAllByDate(appointmentDto.getDate());
        if (optionalList.isPresent() && !optionalList.get().isEmpty()) {
            throw new AppServiceException(EXCEPTION_DATE_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        int l1 = appointmentDto.getEnd().compareTo(appointmentDto.getStart());
        if (l1 < 0) {
            throw new AppServiceException(EXCEPTION_START_END_FORMAT_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        List<AppointmentDto> appointmentDtos = appointmentUtils.splitter(appointmentDto);

        List<Appointment> savedAppointments = appointmentRepository.saveAll(
                appointmentDtos.stream()
                        .map(dto -> appointmentRepository.save(appointmentUtils.mapToEntity(dto)))
                        .toList()
        );
        return savedAppointments.stream()
                .map(appointmentUtils::mapToDto)
                .toList();
    }

    @Override
    public List<AppointmentDto> readAppointments(String dateStr, int page, int size, String statusStr, String sortDirection) {

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));

        Status status = null;
        if (statusStr != null && !statusStr.equalsIgnoreCase(GENERAL_STATUS_ALL)) {
            status = Status.valueOf(statusStr.toUpperCase());
        }

        Sort sort;
        if (sortDirection.equalsIgnoreCase(GENERAL_SORT_DIRECTION_ASC_FIELD)) {
            sort = Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD);
        } else {
            sort = Sort.by(Sort.Direction.DESC, APPOINTMENT_TIME_FIELD);
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Appointment> appointmentPage;
        if (status == null) {
            appointmentPage = appointmentRepository.findAllByDate(pageable, date);
        } else {
            appointmentPage = appointmentRepository.findAllByDateAndStatus(pageable, date, status);
        }

        appointmentUtils.printLogger(log, appointmentPage);

        return appointmentPage
                .get()
                .map(appointmentUtils::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public boolean removeAnAppointment(String publicId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(publicId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            if (appointment.getStatus() == Status.OPEN) {
                appointment.setStatus(Status.DELETED);
                appointmentRepository.save(appointment);
                return true;
            } else if (appointment.getStatus() == Status.TAKEN) {
                throw new AppServiceException(EXCEPTION_NOT_ACCEPTABLE_RESOURCE_MESSAGE, HttpStatus.NOT_ACCEPTABLE);
            } else { // DELETED
                throw new AppServiceException(EXCEPTION_RESOURCE_ALREADY_DELETED_MESSAGE, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new AppServiceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE, HttpStatus.NOT_FOUND);
        }
    }
}
