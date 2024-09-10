package be.jimsa.reddoctor.ws.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = PATIENT_DATABASE_TABLE_NAME)
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PATIENT_DATABASE_JOIN_ID)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(
            mappedBy = PATIENT_FIELD,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Appointment.class
    )
    private Set<Appointment> appointments;
}
