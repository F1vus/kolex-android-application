package edu.at.kolex.model;

import lombok.Data;

@Data
public class BuyRandomTicketRequest {
    private Long travelId;
    private Long userId;
    private Long profileId;
    private int startStop;
    private int endStop;
}
