package com.debosz.kamil.service.carstop;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.carstop.CarStopStatus;
import com.debosz.kamil.domain.carstop.CarStopType;
import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.repository.CarStopRepository;
import com.debosz.kamil.util.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarStopService implements ICarStopService {

    private CarStopRepository carStopRepository;

    private static final Integer TICKET_CODE_LENGTH = 6;

    public CarStopService(CarStopRepository carStopRepository) {
        this.carStopRepository = carStopRepository;
    }

    @Override
    public Optional<CarStop> getByTicketCode(String ticketCode) {
        return carStopRepository.findByTicketCode(ticketCode);
    }

    @Override
    public Optional<CarStop> getByDriverAndCarStopStatus(Driver driver, CarStopStatus carStopStatus) {
        return carStopRepository.findByDriverAndCarStopStatus(driver, carStopStatus);
    }

    @Override
    public CarStop save(CarStop carStop) {
        carStop.setTicketCode(CodeGenerator.generateCode(TICKET_CODE_LENGTH));
        return carStopRepository.save(carStop);
    }

    @Override
    public CarStop update(CarStop carStop) {
        return carStopRepository.save(carStop);
    }

    @Override
    public Integer findAllEntriesBetween(LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore) {
        return carStopRepository.countAllByEntryTimeBetween(entryTimeAfter, entryTimeBefore);
    }

    @Override
    public Integer findAllDeparturesBetween(LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore) {
        return carStopRepository.countAllByDepartureTimeBetween(entryTimeAfter, entryTimeBefore);
    }

    @Override
    public Integer findAllEntriesWithCarStopTypeBetween(CarStopType carStopType, LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore) {
        return carStopRepository.countAllByCarStopTypeAndEntryTimeBetween(carStopType, entryTimeAfter, entryTimeBefore);
    }

    @Override
    public Integer findAllDeparturesWithCarStopTypeBetween(CarStopType carStopType, LocalDateTime entryTimeAfter, LocalDateTime entryTimeBefore) {
        return carStopRepository.countAllByCarStopTypeAndDepartureTimeBetween(carStopType, entryTimeAfter, entryTimeBefore);
    }
}
