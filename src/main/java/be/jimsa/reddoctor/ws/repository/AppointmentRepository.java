package be.jimsa.reddoctor.ws.repository;


import be.jimsa.reddoctor.ws.model.entity.Appointment;
import be.jimsa.reddoctor.ws.model.entity.Patient;
import be.jimsa.reddoctor.ws.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByDate(LocalDate date);

    Page<Appointment> findAllByDate(Pageable pageable, LocalDate date);

    Optional<Appointment> findByPublicId(String publicId);

    Page<Appointment> findAllByDateAndStatus(Pageable pageable, LocalDate date, Status status);

    Page<Appointment> findAllByPatientIsNullAndDate(Pageable pageable, LocalDate date);

    Page<Appointment> findAllByPatient(Pageable pageable, Patient patient);
}
