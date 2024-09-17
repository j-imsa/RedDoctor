package be.jimsa.reddoctor.ws.model.entity;


import be.jimsa.reddoctor.config.validation.annotation.ValidPublicId;
import be.jimsa.reddoctor.config.validation.annotation.ValidTimeSequence;
import be.jimsa.reddoctor.ws.model.enums.Status;
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
@ValidTimeSequence(
        currentTime = APPOINTMENT_VALIDATION_ENTITY_START_TIME_FIELD,
        nextTime = APPOINTMENT_VALIDATION_ENTITY_END_TIME_FIELD,
        message = APPOINTMENT_VALIDATION_SEQUENCE_TIME_MESSAGE
)
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = APPOINTMENT_DATABASE_JOIN_ID)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    @ValidPublicId
    private String publicId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = PATIENT_DATABASE_JOIN_ID, referencedColumnName = PATIENT_DATABASE_JOIN_ID, nullable = true)
    private Patient patient;

}
