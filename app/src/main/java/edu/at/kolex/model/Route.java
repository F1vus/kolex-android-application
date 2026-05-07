package edu.at.kolex.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Route implements Serializable {
    private Long travelId;
    private String trainName;
    private String fromStationName;
    private String toStationName;
    private String actualDeparture;
    private Double price;
}