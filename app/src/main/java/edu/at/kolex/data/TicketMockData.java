package edu.at.kolex.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.model.Route;
import edu.at.kolex.model.Ticket;

public class TicketMockData {

    public static List<Ticket> getMockTickets() {

        List<Ticket> tickets = new ArrayList<>();

        tickets.add(createTicket(
                1L,
                LocalDateTime.of(2024, 6, 15, 8, 30),
                "89.99",
                101L,
                1L,
                1,
                5,
                LocalDateTime.of(2024, 6, 14, 15, 45),
                "Warszawa Centralna",
                "Kraków Główny",
                "IC 1620"
        ));

        tickets.add(createTicket(
                2L,
                LocalDateTime.of(2024, 6, 20, 12, 15),
                "67.50",
                102L,
                1L,
                2,
                4,
                LocalDateTime.of(2024, 6, 19, 14, 20),
                "Warszawa Wschodnia",
                "Wrocław Główny",
                "TLK 2300"
        ));

        tickets.add(createTicket(
                3L,
                LocalDateTime.of(2024, 7, 1, 16, 45),
                "125.00",
                103L,
                1L,
                1,
                6,
                LocalDateTime.of(2024, 6, 25, 10, 30),
                "Gdańsk Główny",
                "Poznań Główny",
                "EIC 103"
        ));

        return tickets;
    }

    private static Ticket createTicket(
            Long ticketId,
            LocalDateTime departureDate,
            String price,
            Long travelId,
            Long profileId,
            Integer startStop,
            Integer endStop,
            LocalDateTime createdAt,
            String startStation,
            String endStation,
            String trainNumber
    ) {

        Route route = null;
        Ticket ticket = new Ticket(
                price,
                route.getTrainNumber());

        ticket.setProfileId(profileId);
        ticket.setStartStation(startStation);
        ticket.setEndStation(endStation);
        ticket.setTrainNumber(trainNumber);

        return ticket;
    }
}