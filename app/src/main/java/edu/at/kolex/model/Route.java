package edu.at.kolex.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Route implements Serializable {
    private final Long travelId;
    private final String trainName;
    private final String fromStationName;
    private final String toStationName;
    private final String actualDeparture;
    private final String  actualArrival;
    private final String duration;
    private final Double price;
}