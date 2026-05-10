package edu.at.kolex.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class TopUpResponse {
    private BigDecimal newBalance;
    private String message;
}