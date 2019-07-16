package com.debosz.kamil.parking.web.rest;

import com.debosz.kamil.domain.carstop.CarStopType;
import com.debosz.kamil.domain.parking.Parking;
import com.debosz.kamil.domain.parking.ParkingAction;
import com.debosz.kamil.dto.ResponseDTO;
import com.debosz.kamil.parking.web.util.DayFormat;
import com.debosz.kamil.service.carstop.ICarStopService;
import com.debosz.kamil.service.parking.IParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private IParkingService parkingService;
    private ICarStopService carStopService;

    private static final String DAY_DATE_FORMAT = "dd/MM/yyyy";
    private static final String DAY_DATE_REGEX = "^\\d{2}/\\d{2}/\\d{4}$";

    private static final String MONTH_DATE_FORMAT = "MM/yyyy";
    private static final String MONTH_DATE_REGEX = "^\\d{2}/\\d{4}$";

    private static final String YEAR_DATE_FORMAT = "yyyy";
    private static final String YEAR_DATE_REGEX = "^\\d{4}$";


    public StatisticsController(IParkingService parkingService, ICarStopService carStopService) {
        this.parkingService = parkingService;
        this.carStopService = carStopService;
    }

    @GetMapping("/parked/all")
    public ResponseEntity<ResponseDTO<Integer>> parkedAll() {
        Parking parking = parkingService.getParking();
        Integer parkedAll = parking.getTakenRegularSpotsNumber() + parking.getTakenSubscriptionSpotsNumber();

        return new ResponseEntity<>(ResponseDTO.of("There is/are " + parkedAll + " cars parked right now.", parkedAll), HttpStatus.OK);
    }

    @GetMapping("/parked/ticket")
    public ResponseEntity<ResponseDTO<Integer>> parkedTicket() {
        Parking parking = parkingService.getParking();
        Integer parkedWithTicket = parking.getTakenRegularSpotsNumber();

        return new ResponseEntity<>(ResponseDTO.of("There is/are " + parkedWithTicket + " cars parked with ticket right now.", parkedWithTicket), HttpStatus.OK);
    }

    @GetMapping("/parked/subscription")
    public ResponseEntity<ResponseDTO<Integer>> parkedSubscription() {
        Parking parking = parkingService.getParking();
        Integer parkedWithSubscription = parking.getTakenSubscriptionSpotsNumber();

        return new ResponseEntity<>(ResponseDTO.of("There is/are " + parkedWithSubscription + " cars parked with subscription right now.", parkedWithSubscription), HttpStatus.OK);
    }

    @GetMapping("dateformat/day")
    public ResponseEntity<String> dayDateFormat() {
        return new ResponseEntity<>(DAY_DATE_FORMAT, HttpStatus.OK);
    }

    @GetMapping("dateformat/month")
    public ResponseEntity<String> monthDateFormat() {
        return new ResponseEntity<>(MONTH_DATE_FORMAT, HttpStatus.OK);
    }

    @GetMapping("dateformat/year")
    public ResponseEntity<String> yearDateFormat() {
        return new ResponseEntity<>(YEAR_DATE_FORMAT, HttpStatus.OK);
    }

    @PostMapping("/entries/all")
    public ResponseEntity<ResponseDTO<Integer>> allEntries(@RequestBody String date) {
        Optional<Integer> entries = getStatistics(ParkingAction.ENTRY, CarStopType.ALL, date);

        return entries
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " car entries for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/departures/all")
    public ResponseEntity<ResponseDTO<Integer>> allDepartures(@RequestBody String date) {
        Optional<Integer> departures = getStatistics(ParkingAction.DEPARTURE, CarStopType.ALL, date);

        return departures
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " car departures for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/entries/ticket")
    public ResponseEntity<ResponseDTO<Integer>> ticketEntries(@RequestBody String date) {
        Optional<Integer> ticketEntries = getStatistics(ParkingAction.ENTRY, CarStopType.TICKET, date);

        return ticketEntries
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " ticket car entries for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/departures/ticket")
    public ResponseEntity<ResponseDTO<Integer>> ticketDepartures(@RequestBody String date) {
        Optional<Integer> ticketDepartures = getStatistics(ParkingAction.DEPARTURE, CarStopType.TICKET, date);

        return ticketDepartures
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " ticket car departures for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/entries/subscription")
    public ResponseEntity<ResponseDTO<Integer>> subscriptionEntries(@RequestBody String date) {
        Optional<Integer> subscriptionEntries = getStatistics(ParkingAction.ENTRY, CarStopType.SUBSCRIPTION, date);

        return subscriptionEntries
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " subscription car entries for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/departures/subscription")
    public ResponseEntity<ResponseDTO<Integer>> subscriptionDepartures(@RequestBody String date) {
        Optional<Integer> subscriptionDepartures = getStatistics(ParkingAction.DEPARTURE, CarStopType.SUBSCRIPTION, date);

        return subscriptionDepartures
                .map(integer -> new ResponseEntity<>(ResponseDTO.of("There was " + integer + " subscription car departures for date: " + date + ".", integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseDTO.of("Invalid date format!" + date + ".", null), HttpStatus.BAD_REQUEST));
    }

    private Optional<Integer> getStatistics(ParkingAction parkingAction, CarStopType carStopType, String date) {

        DayFormat dayFormat;

        if (date.matches(DAY_DATE_REGEX)) {
            dayFormat = DayFormat.DAY;
        } else if (date.matches(MONTH_DATE_REGEX)) {
            dayFormat = DayFormat.MONTH;
        } else if (date.matches(YEAR_DATE_REGEX)) {
            dayFormat = DayFormat.YEAR;
        } else {
            return Optional.empty();
        }

        LocalDateTime dateTimeAfter;
        LocalDateTime dateTimeBefore;

        Integer day;
        Integer month;
        Integer year;

        switch (dayFormat) {
            case DAY:
                day = Integer.valueOf(date.split("/")[0]);
                month = Integer.valueOf(date.split("/")[1]);
                year = Integer.valueOf(date.split("/")[2]);

                dateTimeAfter = LocalDateTime.of(year, month, day, 0, 0, 0);
                dateTimeBefore = LocalDateTime.of(year, month, day, 23, 59, 59);
                break;
            case MONTH:
                month = Integer.valueOf(date.split("/")[0]);
                year = Integer.valueOf(date.split("/")[1]);

                dateTimeAfter = LocalDateTime.of(year, month, 1, 0, 0, 0);
                dateTimeBefore = LocalDateTime.of(year, month + 1, 1, 0, 0, 0);
                break;
            case YEAR:
                year = Integer.valueOf(date);

                dateTimeAfter = LocalDateTime.of(year, 1, 1, 0, 0, 0);
                dateTimeBefore = LocalDateTime.of(year, 12, 31, 23, 59, 59);
                break;
            default:
                return Optional.empty();
        }

        Integer entries;
        Integer departures;

        if (carStopType == CarStopType.ALL) {
            switch (parkingAction) {
                case ENTRY:
                    entries = carStopService.findAllEntriesBetween(dateTimeAfter, dateTimeBefore);
                    return Optional.of(entries);
                case DEPARTURE:
                    departures = carStopService.findAllDeparturesBetween(dateTimeAfter, dateTimeBefore);
                    return Optional.of(departures);
                default:
                    return Optional.empty();
            }
        } else {
            switch (parkingAction) {
                case ENTRY:
                    entries = carStopService.findAllEntriesWithCarStopTypeBetween(carStopType, dateTimeAfter, dateTimeBefore);
                    return Optional.of(entries);
                case DEPARTURE:
                    departures = carStopService.findAllDeparturesWithCarStopTypeBetween(carStopType, dateTimeAfter, dateTimeBefore);
                    return Optional.of(departures);
                default:
                    return Optional.empty();
            }
        }
    }

}
