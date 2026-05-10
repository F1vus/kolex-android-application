package edu.at.kolex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TopUpRequest {
    private BigDecimal amount;
}