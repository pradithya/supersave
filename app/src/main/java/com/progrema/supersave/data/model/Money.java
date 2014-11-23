package com.progrema.supersave.data.model;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Dell on 11/22/2014.
 */
public class Money {

    private Currency currency;
    private int amount;         //in cents
    private float exchangeRate = 1;

    public Money(Currency currency, int amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
