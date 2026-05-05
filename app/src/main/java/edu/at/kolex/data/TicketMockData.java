package edu.at.kolex.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.model.Ticket;

/**
 * Mock data provider for Tickets
 * Used for development and testing without requiring backend connection
 */
public class TicketMockData {

    /**
     * Generate mock tickets for testing
     * @return List of mock ticket objects
     */
    public static List<Ticket> getMockTickets() {
        List<Ticket> tickets = new ArrayList<>();

        // Ticket 1: Warszawa to Kraków
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

        // Ticket 2: Warszawa to Wrocław
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

        // Ticket 3: Gdańsk to Poznań
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

        // Ticket 4: Łódź to Warsaw
        tickets.add(createTicket(
                4L,
                LocalDateTime.of(2024, 7, 5, 9, 0),
                "45.00",
                104L,
                1L,
                3,
                2,
                LocalDateTime.of(2024, 7, 1, 11, 15),
                "Łódź Fabryczna",
                "Warszawa Centralna",
                "TLK 2500"
        ));

        // Ticket 5: Kraków to Katowice
        tickets.add(createTicket(
                5L,
                LocalDateTime.of(2024, 7, 10, 14, 20),
                "52.50",
                105L,
                1L,
                2,
                3,
                LocalDateTime.of(2024, 7, 8, 16, 45),
                "Kraków Główny",
                "Katowice",
                "IC 2000"
        ));

        // Ticket 6: Wrocław to Berlin (International)
        tickets.add(createTicket(
                6L,
                LocalDateTime.of(2024, 7, 15, 6, 30),
                "199.99",
                106L,
                1L,
                1,
                8,
                LocalDateTime.of(2024, 7, 10, 13, 30),
                "Wrocław Główny",
                "Berlin Hbf",
                "EIC 44"
        ));

        // Ticket 7: Szczecin to Poznań
        tickets.add(createTicket(
                7L,
                LocalDateTime.of(2024, 7, 20, 11, 0),
                "78.00",
                107L,
                1L,
                4,
                2,
                LocalDateTime.of(2024, 7, 18, 9, 20),
                "Szczecin Główny",
                "Poznań Główny",
                "TLK 2600"
        ));

        // Ticket 8: Warsaw to Gdańsk (Fast train)
        tickets.add(createTicket(
                8L,
                LocalDateTime.of(2024, 7, 25, 13, 30),
                "95.50",
                108L,
                1L,
                1,
                7,
                LocalDateTime.of(2024, 7, 23, 10, 0),
                "Warszawa Centralna",
                "Gdańsk Główny",
                "PKP IC"
        ));

        // Ticket 9: Krakow to Lviv (Near future)
        tickets.add(createTicket(
                9L,
                LocalDateTime.of(2024, 8, 5, 15, 45),
                "135.00",
                109L,
                1L,
                3,
                5,
                LocalDateTime.of(2024, 7, 30, 12, 30),
                "Kraków Główny",
                "Lviv",
                "EIC 11"
        ));

        // Ticket 10: Poznań to Warsaw
        tickets.add(createTicket(
                10L,
                LocalDateTime.of(2024, 8, 10, 10, 15),
                "72.00",
                110L,
                1L,
                2,
                2,
                LocalDateTime.of(2024, 8, 8, 14, 45),
                "Poznań Główny",
                "Warszawa Centralna",
                "TLK 2800"
        ));

        return tickets;
    }

    /**
     * Get a single mock ticket by ID
     * @param ticketId The ticket ID to find
     * @return Ticket if found, null otherwise
     */
    public static Ticket getMockTicketById(Long ticketId) {
        List<Ticket> allTickets = getMockTickets();
        for (Ticket ticket : allTickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Get mock tickets filtered by profile ID
     * @param profileId The profile ID
     * @return List of tickets for that profile
     */
    public static List<Ticket> getMockTicketsByProfile(Long profileId) {
        List<Ticket> allTickets = getMockTickets();
        List<Ticket> profileTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            if (ticket.getProfileId().equals(profileId)) {
                profileTickets.add(ticket);
            }
        }
        return profileTickets;
    }

    /**
     * Helper method to create a ticket with all fields
     */
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
        Ticket ticket = new Ticket(
                ticketId,
                departureDate,
                price,
                travelId,
                startStop,
                endStop,
                createdAt
        );
        ticket.setStartStation(startStation);
        ticket.setEndStation(endStation);
        ticket.setTrainNumber(trainNumber);
        ticket.setProfileId(profileId);
        return ticket;
    }

    /**
     * Get a limited set of mock tickets for quick testing
     * @param count Number of tickets to return
     * @return List of mock tickets (limited)
     */
    public static List<Ticket> getMockTicketsLimited(int count) {
        List<Ticket> allTickets = getMockTickets();
        return allTickets.subList(0, Math.min(count, allTickets.size()));
    }

    /**
     * Get mock tickets filtered by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of tickets within the date range
     */
    public static List<Ticket> getMockTicketsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Ticket> allTickets = getMockTickets();
        List<Ticket> filteredTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            if (ticket.getDepartureDate() != null &&
                    ticket.getDepartureDate().isAfter(startDate) &&
                    ticket.getDepartureDate().isBefore(endDate)) {
                filteredTickets.add(ticket);
            }
        }
        return filteredTickets;
    }
}
