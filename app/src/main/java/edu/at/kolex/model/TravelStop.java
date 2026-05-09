package edu.at.kolex.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelStop {
    private int    stopNumber;
    private String stationName;
    private String arrivalOffset;
    private String departureOffset;
    private int    distance;
}