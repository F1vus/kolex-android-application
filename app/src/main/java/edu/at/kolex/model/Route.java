package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Route {
    private  String trainNumber;
    private  String departureTime;
    private  String arrivalTime;
    private  String duration;
    private  String price;
}