package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class SeatStatus {
    private Long seatId;
    private int seatNumber;
    private boolean available;
}
