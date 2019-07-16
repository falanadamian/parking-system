package com.debosz.kamil.domain.carstop;

import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.payment.Payment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CarStop {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ticket_code")
    private String ticketCode;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_stop_status")
    private CarStopStatus carStopStatus;

    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "car_stop_type")
    private CarStopType carStopType;

    @JsonIgnore
    public Payment getLatestPayment() {
        payments.sort(Comparator.comparing(Payment::getPaymentTime));
        return payments.get(payments.size() - 1);
    }
}
