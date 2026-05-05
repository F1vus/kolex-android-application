package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Ticket implements Serializable {
    private Long ticketId;
    private LocalDateTime departureDate;
    private String ticketPrice;
    private Long travelId;
    private Long profileId;
    private Integer startStopNumber;
    private Integer endStopNumber;
    private LocalDateTime ticketCreatedAt;
    
    // Additional fields for display purposes
    private String startStation;
    private String endStation;
    private String trainNumber;

    // Constructor for basic ticket info
    public Ticket(Long ticketId, LocalDateTime departureDate, String ticketPrice, 
                  Long travelId, Integer startStopNumber, Integer endStopNumber,
                  LocalDateTime ticketCreatedAt) {
        this.ticketId = ticketId;
        this.departureDate = departureDate;
        this.ticketPrice = ticketPrice;
        this.travelId = travelId;
        this.startStopNumber = startStopNumber;
        this.endStopNumber = endStopNumber;
        this.ticketCreatedAt = ticketCreatedAt;
    }
}
