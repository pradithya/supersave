package com.progrema.supersave.data.model;

import android.content.ContentValues;

import com.progrema.supersave.data.db.SupersaveContract;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Dell on 11/22/2014.
 */
public class User {
    private String name;
    private Currency defaultCurrency;
    private Money defaultMonthlyBudget;

    private Money remainingBudget;
    private int cycleDate;
    private int activeBudgetId;
    private Money totalSaving;

    public User(String name, Currency defaultCurrency, Money monthlyBudget, int cycleDate) {
        this.name = name;
        this.defaultCurrency = defaultCurrency;
        this.defaultMonthlyBudget = monthlyBudget;
        this.remainingBudget = monthlyBudget;
        this.cycleDate = cycleDate;
        this.activeBudgetId = 0;
        totalSaving = new Money(defaultCurrency, 0);
    }

    public Money getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Money remainingBudget) {
        this.remainingBudget = remainingBudget;
    }


    public int getCycleDate() {
        return cycleDate;
    }

    public ContentValues getContentValue() {
        ContentValues values = new ContentValues();
        values.put(SupersaveContract.User.USER_NAME, name);
        values.put(SupersaveContract.User.DEFAULT_CURRENCY, defaultCurrency.getCurrencyCode());
        values.put(SupersaveContract.User.DEFAULT_MONTHLY_BUDGET, defaultMonthlyBudget.getAmount());
        values.put(SupersaveContract.User.CYCLE_DATE, cycleDate);
        values.put(SupersaveContract.User.ACTIVE_BUDGET_ID, activeBudgetId);
        values.put(SupersaveContract.User.TOTAL_SAVING, totalSaving.getAmount());
        return values;
    }

}
