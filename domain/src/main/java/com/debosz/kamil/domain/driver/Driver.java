package com.debosz.kamil.domain.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private PersonalInformation personalInformation;

    @NotNull
    @Column(name = "card_code", unique = true)
    private String cardCode;

    @Column(name = "expiration_time")
    private LocalDate expirationTime;
}
