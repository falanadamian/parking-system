package com.debosz.kamil.parkingstatistics.service;

import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class InterfaceService {

    private static final String INITIAL_MESSAGE = "Parking statistics service:" +
            "\n Press [1] to get number of cars parked now." +
            "\n Press [2] to get number of cars parked with ticket now." +
            "\n Press [3] to get number of cars parked with subscription card now." +
            "\n Press [4] to get number of all entries on specified date." +
            "\n Press [5] to get number of ticket entries on specified date." +
            "\n Press [6] to get number of subscription entries on specified date." +
            "\n Press [7] to get number of all departures on specified date." +
            "\n Press [8] to get number of ticket departures on specified date." +
            "\n Press [9] to get number of subscription departures on specified date." +
            "\n Press [0] to exit program..." +
            "\n Waiting...";

    private ParkingStatisticsService parkingStatisticsService;

    public InterfaceService(ParkingStatisticsService parkingStatisticsService) {
        this.parkingStatisticsService = parkingStatisticsService;

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

        ResponseDTO<Integer> response;
        String date;

        switch (selectedOption) {
            case 0:
                System.exit(-1);
                break;
            case 1:
                System.out.println("Fetching data, please wait...:");
                response = parkingStatisticsService.getAllParkedNow();
                System.out.println(response.getMessage());
                break;
            case 2:
                System.out.println("Fetching data, please wait...:");
                response = parkingStatisticsService.getTicketParkedNow();
                System.out.println(response.getMessage());
                break;
            case 3:
                System.out.println("Fetching data, please wait...:");
                response = parkingStatisticsService.getSubscriptionParkedNow();
                System.out.println(response.getMessage());
                break;
            case 4:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getAllEntriesOnDate(date);
                System.out.println(response.getMessage());
                break;
            case 5:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getTicketEntriesOnDate(date);
                System.out.println(response.getMessage());
                break;
            case 6:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getSubscriptionEntriesOnDate(date);
                System.out.println(response.getMessage());
                break;
            case 7:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getAllDeparturesOnDate(date);
                System.out.println(response.getMessage());
                break;
            case 8:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getTicketDeparturesOnDate(date);
                System.out.println(response.getMessage());
                break;
            case 9:
                displayDateFormatsAndAskForInput();
                date = scanner.nextLine();
                response = parkingStatisticsService.getSubscriptionDeparturesOnDate(date);
                System.out.println(response.getMessage());
                break;
        }

        System.out.print("\n\n");
    }

    private void displayDateFormats() {
        System.out.println("Available date formats:");
        System.out.println(parkingStatisticsService.getDayDateFormat());
        System.out.println(parkingStatisticsService.getMonthDateFormat());
        System.out.println(parkingStatisticsService.getYearDateFormat());
    }

    private void displayDateFormatsAndAskForInput() {
        System.out.println("Fetching data, please wait...:");
        displayDateFormats();
        System.out.println("Enter date:");
    }


}
