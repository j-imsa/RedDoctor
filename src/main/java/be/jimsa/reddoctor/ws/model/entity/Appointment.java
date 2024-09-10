package be.jimsa.reddoctor.ws.model.entity;


import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = APPOINTMENT_DATABASE_TABLE_NAME)
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = APPOINTMENT_DATABASE_JOIN_ID)
    private Long id;

    @Column(nullable = false, unique = true)
    @ValidPublicId
    private String publicId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = PATIENT_DATABASE_JOIN_ID, referencedColumnName = PATIENT_DATABASE_JOIN_ID, nullable = true)
    private Patient patient;

}
