package edu.at.kolex.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {

    private Long ticketId;
    private LocalDateTime departureDate;
    private String price;
    private Long travelId;
    private Integer startStop;
    private Integer endStop;
    private LocalDateTime createdAt;
    private String trainNumber;
    private String startStation;
    private String endStation;
    private Long profileId;

    public Ticket(String price,
                  String trainNumber) {

        this.ticketId = ticketId;
        this.departureDate = departureDate;
        this.price = price;
        this.travelId = travelId;
        this.startStop = startStop;
        this.endStop = endStop;
        this.createdAt = createdAt;
        this.trainNumber = trainNumber;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public Long getTicketId() { return ticketId; }
    public LocalDateTime getDepartureDate() { return departureDate; }
    public String getPrice() { return price; }
    public Long getTravelId() { return travelId; }
    public Integer getStartStop() { return startStop; }
    public Integer getEndStop() { return endStop; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getTrainNumber() { return trainNumber; }
    public String getStartStation() { return startStation; }
    public String getEndStation() { return endStation; }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
}