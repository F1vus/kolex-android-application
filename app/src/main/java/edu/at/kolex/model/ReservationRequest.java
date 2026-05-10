package edu.at.kolex.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationRequest {
    private Long seatId;
    private Long profileId;
    private Integer startStopNumber;
    private Integer endStopNumber;
}

