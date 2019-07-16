package com.debosz.kamil.service.carstop;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.carstop.CarStopStatus;
import com.debosz.kamil.domain.carstop.CarStopType;
import com.debosz.kamil.domain.driver.Driver;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ICarStopService {

    Optional<CarStop> getByTicketCode(String ticketCode);

    Optional<CarStop> getByDriverAndCarStopStatus(Driver driver, CarStopStatus carStopStatus);

    CarStop save(CarStop carStop);

    CarStop update(CarStop carStop);

    Integer findAllEntriesBetween(LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);

    Integer findAllDeparturesBetween(LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);

    Integer findAllEntriesWithCarStopTypeBetween(CarStopType carStopType, LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);

    Integer findAllDeparturesWithCarStopTypeBetween(CarStopType carStopType, LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore);
}
