package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {
    private  Long id;
    private  String trainName;
    private  String profileName;
    private  String fromStationName;
    private  String toStationName;
    private  Integer seatNumber;
    private  LocalDateTime actualDeparture;
    private  LocalDateTime actualArrival;
    private  BigDecimal price;
    private  Integer travelStopNumberFrom;
    private  Integer travelStopNumberTo;
}