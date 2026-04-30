package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Route {
    private final String trainNumber;
    private final String departureTime;
    private final String arrivalTime;
    private final String duration;
    private final String price;
}