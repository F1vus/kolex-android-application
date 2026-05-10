package edu.at.kolex.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelStop {
    private Integer    stopNumber;
    private String stationName;
    private String arrivalOffset;
    private String departureOffset;
    private Integer    distance;
}