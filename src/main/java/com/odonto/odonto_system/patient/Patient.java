package com.odonto.odonto_system.patient;

import com.odonto.odonto_system.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    private String phone;

    private String email;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    private boolean active = true;

}
