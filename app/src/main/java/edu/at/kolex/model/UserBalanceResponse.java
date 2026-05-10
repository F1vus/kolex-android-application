package edu.at.kolex.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UserBalanceResponse {
    private BigDecimal balance;
}