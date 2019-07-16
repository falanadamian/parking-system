package com.debosz.kamil.repository;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.carstop.CarStopStatus;
import com.debosz.kamil.domain.carstop.CarStopType;
import com.debosz.kamil.domain.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CarStopRepository extends JpaRepository<CarStop, Long> {

    Optional<CarStop> findByTicketCode(String ticketCode);

    Optional<CarStop> findByDriverAndCarStopStatus(Driver driver, CarStopStatus carStopStatus);

    Integer countAllByEntryTimeBetween(LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);

    Integer countAllByDepartureTimeBetween(LocalDateTime departureTimeAfter, LocalDateTime departureTimeBefore);

    Integer countAllByCarStopTypeAndEntryTimeBetween(CarStopType carStopType, LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);

    Integer countAllByCarStopTypeAndDepartureTimeBetween(CarStopType carStopType, LocalDateTime departureTimeAfter, LocalDateTime departureTimeBefore);

}
