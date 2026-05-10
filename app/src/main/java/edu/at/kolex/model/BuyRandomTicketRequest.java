package edu.at.kolex.model;

import lombok.Data;

@Data
public class BuyRandomTicketRequest {
    private Long travelId;
    private Long profileId;
    private int startStop;
    private int endStop;
}
