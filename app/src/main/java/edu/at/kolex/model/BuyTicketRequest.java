package edu.at.kolex.model;

import lombok.Data;

@Data
public class BuyTicketRequest {
    private Long userId;
    private Long reservationId;
}
