package com.debosz.kamil.parking.web.rest;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.carstop.CarStopStatus;
import com.debosz.kamil.domain.carstop.CarStopType;
import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.driver.PersonalInformation;
import com.debosz.kamil.domain.payment.Payment;
import com.debosz.kamil.dto.ResponseDTO;
import com.debosz.kamil.parking.web.util.ParkingConstants;
import com.debosz.kamil.service.carstop.ICarStopService;
import com.debosz.kamil.service.driver.IDriverService;
import com.debosz.kamil.service.parking.IParkingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/parking")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ParkingController {

    private IParkingService parkingService;
    private ICarStopService carStopService;
    private IDriverService driverService;

    public ParkingController(IParkingService parkingService, ICarStopService carStopService, IDriverService driverService) {
        this.parkingService = parkingService;
        this.carStopService = carStopService;
        this.driverService = driverService;
    }

    @GetMapping("/entry/regular")
    public ResponseEntity<ResponseDTO<CarStop>> regularEntry() {

        if (!parkingService.isSlotAvailable()) {
            return new ResponseEntity<>(ResponseDTO.of("Sorry, but there are no available spots left.", null), HttpStatus.FORBIDDEN);
        }

        CarStop carStop = CarStop.builder().carStopStatus(CarStopStatus.OPENED).entryTime(LocalDateTime.now()).carStopType(CarStopType.TICKET).build();
        carStop = carStopService.save(carStop);

        parkingService.incrementTakenRegularSpotsNumber();

        log.info("Raising the tollgate");
        return new ResponseEntity<>(ResponseDTO.of("Please get your parking ticket and save it for departure.", carStop), HttpStatus.OK);
    }

    @PostMapping("/entry/subscription")
    public ResponseEntity<String> subscriptionEntry(@RequestBody String cardCode) {
        Optional<Driver> optionalDriver = driverService.getByCardCode(cardCode);

        if (!optionalDriver.isPresent()) {
            return new ResponseEntity<>("Card is not recognized. Please contact following phone number: 111 111 111.", HttpStatus.FORBIDDEN);
        }

        Driver driver = optionalDriver.get();

        if (driver.getExpirationTime().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Your card has expired. Get regular ticket or renew card.", HttpStatus.FORBIDDEN);
        }

        if (!parkingService.isSlotAvailable()) {
            return new ResponseEntity<>("You just bought your card, didn't you?. After first parking spot release, we will hold free spot for you.", HttpStatus.FORBIDDEN);
        }

        CarStop carStop = CarStop.builder().driver(driver).carStopStatus(CarStopStatus.OPENED).entryTime(LocalDateTime.now()).carStopType(CarStopType.SUBSCRIPTION).build();
        carStopService.save(carStop);

        parkingService.incrementTakenSubscriptionSpotsNumber();

        String cardExpirationDate = driver.getExpirationTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        log.info("Raising the tollgate");
        return new ResponseEntity<>("Welcome! Remember to renew your card before " + cardExpirationDate + ". Enjoy your stay!", HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<ResponseDTO<CarStop>> payment(@RequestBody String ticketCode) {
        Optional<CarStop> optionalCarStop = carStopService.getByTicketCode(ticketCode);

        if (!optionalCarStop.isPresent()) {
            return new ResponseEntity<>(ResponseDTO.of("Ticket not recognized!.", null), HttpStatus.FORBIDDEN);
        }

        CarStop carStop = optionalCarStop.get();

        double secondsParked;

        if (carStop.getPayments().isEmpty()) {
            secondsParked = (double) ChronoUnit.SECONDS.between(carStop.getEntryTime(), LocalDateTime.now());
        } else {
            secondsParked = (double) ChronoUnit.SECONDS.between(carStop.getLatestPayment().getPaymentTime(), LocalDateTime.now());
        }

        BigDecimal paymentAmount = BigDecimal.valueOf(Math.ceil(secondsParked / 3600) * ParkingConstants.HOURLY_RATE);

        Payment payment = Payment.builder().payment(paymentAmount).paymentTime(LocalDateTime.now()).build();
        carStop.getPayments().add(payment);
        carStop.setCarStopStatus(CarStopStatus.PAID);

        carStopService.update(carStop);

        return new ResponseEntity<>(ResponseDTO.of("To pay: " + paymentAmount + "PLN. Payment successful, thank you for your visit!", carStop), HttpStatus.OK);
    }

    @PostMapping("/departure/regular")
    public ResponseEntity<ResponseDTO<CarStop>> regularDeparture(@RequestBody String ticketCode) {
        Optional<CarStop> optionalCarStop= carStopService.getByTicketCode(ticketCode);

        if (!optionalCarStop.isPresent()) {
            return new ResponseEntity<>(ResponseDTO.of("Ticket not recognized!.", null), HttpStatus.FORBIDDEN);
        }

        CarStop carStop = optionalCarStop.get();

        if(carStop.getPayments().isEmpty()) {
            return new ResponseEntity<>(ResponseDTO.of("You have to make a payment before you departure!", null), HttpStatus.FORBIDDEN);
        }

        Long timeAfterPayment = ChronoUnit.MINUTES.between(carStop.getLatestPayment().getPaymentTime(), LocalDateTime.now());

        if (timeAfterPayment > 3) {
            carStop.setCarStopStatus(CarStopStatus.OVERDUE);
            return new ResponseEntity<>(ResponseDTO.of("3 minutes have passed since the payment. Ticket is inactive. Make the payment again.", carStop), HttpStatus.FORBIDDEN);
        }

        parkingService.decrementTakenRegularSpotsNumber();

        carStop.setCarStopStatus(CarStopStatus.CLOSED);
        carStop.setDepartureTime(LocalDateTime.now());

        carStopService.update(carStop);

        log.info("Raising the tollgate");
        return new ResponseEntity<>(ResponseDTO.of("See you later!", carStop), HttpStatus.OK);
    }

    @PostMapping("/departure/subscription")
    public ResponseEntity<String> subscriptionDeparture(@RequestBody String cardCode) {
        Optional<Driver> optionalDriver = driverService.getByCardCode(cardCode);

        if (!optionalDriver.isPresent()) {
            return new ResponseEntity<>("Card not recognized!", HttpStatus.FORBIDDEN);
        }

        Optional<CarStop> optionalCarStop = carStopService.getByDriverAndCarStopStatus(optionalDriver.get(), CarStopStatus.OPENED);

        if (!optionalCarStop.isPresent()) {
            return new ResponseEntity<>("No car park is opened with this car.", HttpStatus.FORBIDDEN);
        }

        CarStop carStop = optionalCarStop.get();

        parkingService.decrementTakenSubscriptionSpotsNumber();

        carStop.setCarStopStatus(CarStopStatus.CLOSED);
        carStop.setDepartureTime(LocalDateTime.now());

        carStopService.update(carStop);

        log.info("Raising the tollgate");
        return new ResponseEntity<>("See you later!", HttpStatus.OK);
    }

    @PostMapping("/subscription/issue")
    public ResponseEntity<ResponseDTO<Driver>> issueSubscriptionCard(@RequestBody PersonalInformation personalInformation) {

        if(Strings.isBlank(personalInformation.getFirstName()) || Strings.isBlank(personalInformation.getPhoneNumber())) {
            return new ResponseEntity<>(ResponseDTO.of("Provided data is invalid..", null), HttpStatus.FORBIDDEN);
        }

        Driver driver = driverService.issueSubscriptionCard(personalInformation);

        log.info("Printing subscription card");
        return new ResponseEntity<>(ResponseDTO.of("Your subscription card was successfully created.", driver), HttpStatus.OK);
    }

}
