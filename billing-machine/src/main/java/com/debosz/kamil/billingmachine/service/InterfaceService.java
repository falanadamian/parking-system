package com.debosz.kamil.billingmachine.service;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.driver.PersonalInformation;
import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Scanner;

@Component
public class InterfaceService {

    private static final String INITIAL_MESSAGE = "Billing machine service:" +
            "\n Press [1] to pay for the ticket." +
            "\n Press [2] to issue an subscription card." +
            "\n Press [0] to exit program..." +
            "\n Waiting...";

    private BillingMachineService billingMachineService;

    public InterfaceService(BillingMachineService billingMachineService) {
        this.billingMachineService = billingMachineService;

        while (true) {
            run();
        }
    }

    private void run() {
        System.out.println(INITIAL_MESSAGE);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next().replaceAll("[^0-9]", "");

        if (input.isEmpty()) {
            return;
        }

        Integer selectedOption = Character.getNumericValue(input.charAt(0));

        scanner.nextLine();

        ResponseDTO<CarStop> carStopResponse;
        ResponseDTO<Driver> driverResponse;
        Driver driver;
        String message;

        String ticketCode;

        switch (selectedOption) {
            case 0:
                System.exit(-1);
                break;
            case 1:
                System.out.println("Please enter ticket code:");
                ticketCode = scanner.nextLine();

                carStopResponse = billingMachineService.payForTicket(ticketCode);
                message = carStopResponse.getMessage();
                System.out.println(message);
                break;
            case 2:
                System.out.println("We need some of your personal information. Please enter your first name:");
                String firstName = scanner.nextLine();
                System.out.println("Enter your last name:");
                String lastName = scanner.nextLine();
                System.out.println("Enter your phone number:");
                String phoneNumber = scanner.nextLine();

                PersonalInformation personalInformation = PersonalInformation.builder().firstName(firstName).lastName(lastName).phoneNumber(phoneNumber).build();
                driverResponse = billingMachineService.issueSubscriptionCard(personalInformation);

                driver = driverResponse.getData();
                message = driverResponse.getMessage();

                System.out.println(message);

                if (Objects.isNull(driver)) {
                    break;
                }

                printDriverInformation(driver);
                break;
        }

        System.out.print("\n\n");
    }

    private void printDriverInformation(Driver driver) {
        System.out.println("First name: " + driver.getPersonalInformation().getFirstName());
        System.out.println("Last name: " + driver.getPersonalInformation().getLastName());
        System.out.println("Phone number: " + driver.getPersonalInformation().getPhoneNumber());
        System.out.println("Card code: " + driver.getCardCode());
        System.out.println("Expiration time: " + driver.getExpirationTime());
    }

}
