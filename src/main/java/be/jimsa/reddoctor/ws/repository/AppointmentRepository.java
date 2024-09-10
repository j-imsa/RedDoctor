package be.jimsa.reddoctor.ws.repository;


import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<List<Appointment>> findAllByDate(LocalDate date);

    Optional<Appointment> findByPublicId(String publicId);

    Page<Appointment> findAllByDate(Pageable pageable, LocalDate date);

    Page<Appointment> findAllByPatientIsNullAndDate(Pageable pageable, LocalDate date);

    Page<Appointment> findAllByPatient(Pageable pageable, Patient patient);
}
