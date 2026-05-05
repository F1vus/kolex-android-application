package edu.at.kolex.model;

public class Route {

    private final String trainNumber;
    private final String departureTime;
    private final String arrivalTime;
    private final String duration;
    private final String price;

    public Route(String trainNumber, String departureTime, String arrivalTime, String duration, String price) {
        this.trainNumber = trainNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.price = price;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }
}