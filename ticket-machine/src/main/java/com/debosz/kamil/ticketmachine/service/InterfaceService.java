package com.debosz.kamil.ticketmachine.service;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Scanner;

@Component
public class InterfaceService {

    private static final String INITIAL_MESSAGE = "Welcome in automatic parking system." +
            "\n Press [1] to get a ticket and enter parking." +
            "\n Press [2] to enter parking with your subscription card (type in your card code)." +
            "\n Press [3] to departure with ticket." +
            "\n Press [4] to departure with subscription card (type in your card code)." +
            "\n Press [0] to exit program..." +
            "\n Waiting...";

    private TicketMachineService ticketMachineService;

    public InterfaceService(TicketMachineService ticketMachineService) {
        this.ticketMachineService = ticketMachineService;

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

        scanner.nextLine();

        Integer selectedOption = Character.getNumericValue(input.charAt(0));

        ResponseDTO<CarStop> carStopResponse;
        CarStop carStop;
        String message;

        String ticketCode;
        String cardCode;

        switch (selectedOption) {
            case 0:
                System.exit(-1);
                break;
            case 1:
                System.out.println("Wait...");

                carStopResponse = ticketMachineService.enterParking();
                carStop = carStopResponse.getData();
                message = carStopResponse.getMessage();

                System.out.println(message);

                if (Objects.isNull(carStop)) {
                    break;
                }

                printTicket(carStop);
                break;
            case 2:
                System.out.println("Please enter your card code:");
                ticketCode = scanner.nextLine();

                message = ticketMachineService.enterParkingWithSubscription(ticketCode);
                System.out.println(message);
                break;
            case 3:
                System.out.println("Please enter ticket code:");
                ticketCode = scanner.nextLine();

                carStopResponse = ticketMachineService.departure(ticketCode);
                message = carStopResponse.getMessage();
                System.out.println(message);
                break;
            case 4:
                System.out.println("Please enter card code:");
                cardCode = scanner.nextLine();

                message = ticketMachineService.departureWithSubscription(cardCode);
                System.out.println(message);
                break;
        }

        System.out.print("\n\n");
    }

    private void printTicket(CarStop carStop) {
        System.out.println("Ticket code: " + carStop.getTicketCode());
        System.out.println("Entry time: " + carStop.getEntryTime());
    }

}
