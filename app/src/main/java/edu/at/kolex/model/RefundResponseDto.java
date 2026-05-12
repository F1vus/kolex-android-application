package edu.at.kolex.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseDto {
    private Long ticketId;
    private BigDecimal refundAmount;
    private String message;
}
