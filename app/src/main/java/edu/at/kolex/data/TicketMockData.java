package edu.at.kolex.data;

/*
 * Mock data provider for Tickets
 * Used for development and testing without requiring backend connection
 */
//public class TicketMockData {
//
//    public static List<Ticket> getMockTickets() {
//
//        List<Ticket> tickets = new ArrayList<>();
//
//        tickets.add(createTicket(
//                1L,
//                "IC 1620",
//                "Jan Kowalski",
//                "Warszawa Centralna",
//                "Kraków Główny",
//                45,
//                LocalDateTime.of(2026, 6, 15, 8, 30),
//                LocalDateTime.of(2026, 6, 15, 11, 10),
//                new BigDecimal("89.99"),
//                1,
//                5
//        ));
//
//        tickets.add(createTicket(
//                2L,
//                "TLK 2300",
//                "Anna Nowak",
//                "Warszawa Wschodnia",
//                "Wrocław Główny",
//                18,
//                LocalDateTime.of(2026, 6, 20, 12, 15),
//                LocalDateTime.of(2026, 6, 20, 16, 40),
//                new BigDecimal("67.50"),
//                2,
//                4
//        ));
//
//        tickets.add(createTicket(
//                3L,
//                "EIC 103",
//                "Piotr Wiśniewski",
//                "Gdańsk Główny",
//                "Poznań Główny",
//                7,
//                LocalDateTime.of(2026, 7, 1, 16, 45),
//                LocalDateTime.of(2026, 7, 1, 20, 15),
//                new BigDecimal("125.00"),
//                1,
//                6
//        ));
//
//        tickets.add(createTicket(
//                4L,
//                "TLK 2500",
//                "Maria Zielińska",
//                "Łódź Fabryczna",
//                "Warszawa Centralna",
//                22,
//                LocalDateTime.of(2026, 7, 5, 9, 0),
//                LocalDateTime.of(2026, 7, 5, 10, 45),
//                new BigDecimal("45.00"),
//                3,
//                2
//        ));
//
//        tickets.add(createTicket(
//                5L,
//                "EIP 4515",
//                "Adam Kowalczyk",
//                "Kraków Główny",
//                "Katowice",
//                3,
//                LocalDateTime.of(2026, 7, 10, 14, 20),
//                LocalDateTime.of(2026, 7, 10, 15, 35),
//                new BigDecimal("52.50"),
//                2,
//                3
//        ));
//
//        return tickets;
//    }
//
//    public static Ticket getMockTicketById(Long ticketId) {
//        for (Ticket ticket : getMockTickets()) {
//            if (ticket.getId().equals(ticketId)) {
//                return ticket;
//            }
//        }
//        return null;
//    }
//
//    private static Ticket createTicket(
//            Long id,
//            String trainName,
//            String profileName,
//            String fromStationName,
//            String toStationName,
//            Integer seatNumber,
//            LocalDateTime actualDeparture,
//            LocalDateTime actualArrival,
//            BigDecimal price,
//            Integer travelStopNumberFrom,
//            Integer travelStopNumberTo
//    ) {
//
//        return new Ticket(
//                id,
//                trainName,
//                profileName,
//                fromStationName,
//                toStationName,
//                seatNumber,
//                actualDeparture,
//                actualArrival,
//                price,
//                travelStopNumberFrom,
//                travelStopNumberTo
//        );
//    }
//
//    public static List<Ticket> getMockTicketsLimited(int count) {
//        List<Ticket> allTickets = getMockTickets();
//        return allTickets.subList(0, Math.min(count, allTickets.size()));
//    }
//
//    public static List<Ticket> getMockTicketsByDateRange(
//            LocalDateTime startDate,
//            LocalDateTime endDate
//    ) {
//
//        List<Ticket> filtered = new ArrayList<>();
//
//        for (Ticket ticket : getMockTickets()) {
//
//            if (ticket.getActualDeparture() != null
//                    && !ticket.getActualDeparture().isBefore(startDate)
//                    && !ticket.getActualDeparture().isAfter(endDate)) {
//
//                filtered.add(ticket);
//            }
//        }
//
//        return filtered;
//    }
//}