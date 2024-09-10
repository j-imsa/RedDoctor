package be.jimsa.reddoctor.ws.service.impl;

import be.jimsa.reddoctor.config.exception.NotFoundResourceException;
import be.jimsa.reddoctor.config.exception.ResourceAlreadyExistException;
import be.jimsa.reddoctor.utility.mapper.AppointmentMapper;
import be.jimsa.reddoctor.utility.mapper.PatientMapper;
import be.jimsa.reddoctor.ws.model.dto.AppointmentDto;
import be.jimsa.reddoctor.ws.model.dto.PatientDto;
import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.repository.AppointmentRepository;
import be.jimsa.reddoctor.ws.repository.PatientRepository;
import be.jimsa.reddoctor.ws.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;


    @Override
    public AppointmentDto updateAnAppointment(String publicId, PatientDto patientDto) {

        Optional<Appointment> optionalAppointment = appointmentRepository.findByPublicId(publicId);
        if (optionalAppointment.isEmpty()) {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }
        if (optionalAppointment.get().getPatient() != null) {
            throw new ResourceAlreadyExistException(EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE);
        }

        Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(patientDto.getPhoneNumber());
        Patient savedPatient = optionalPatient
                .map(patient -> patientRepository.save(patientMapper.mapToEntityById(patientDto, patient.getId())))
                .orElseGet(() -> patientRepository.save(patientMapper.mapToEntity(patientDto)));

        Appointment appointment = optionalAppointment.get();
        appointment.setPatient(savedPatient);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.mapToDto(updatedAppointment);
    }

    @Override
    public List<AppointmentDto> readMyAppointments(String phoneNumber, int page, int size, String type, String sortDirection) {

        Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(phoneNumber);
        if (optionalPatient.isEmpty()) {
            // throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_BY_PHONE_NUMBER_MESSAGE);
            return new ArrayList<>();
        }

        Sort sort;
        if (sortDirection.equalsIgnoreCase(APPOINTMENT_SORT_DIRECTION_ASC_FIELD)) {
            sort = Sort.by(Sort.Direction.ASC, APPOINTMENT_TIME_FIELD);
        } else {
            sort = Sort.by(Sort.Direction.DESC, APPOINTMENT_TIME_FIELD);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                sort
        );

        Page<Appointment> appointmentPage = appointmentRepository.findAllByPatient(pageable, optionalPatient.get());

        return appointmentPage
                .get()
                .map(appointmentMapper::mapToDto)
                .toList();
    }
}
