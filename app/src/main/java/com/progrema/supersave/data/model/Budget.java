package com.progrema.supersave.data.model;

import android.content.ContentValues;

import com.progrema.supersave.data.db.SupersaveContract;

import java.util.Date;

/**
 * Created by Dell on 11/22/2014.
 */
public class Budget {

    private Date startDate;
    private Date endDate;
    private Money initialAmount;
    private Money totalExpense;

    public Budget(Date startDate, Date endDate, Money initialAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialAmount = initialAmount;
        this.totalExpense = new Money(initialAmount.getCurrency(), 0);
    }

    public ContentValues appendContentValue(ContentValues cv){
        cv.put(SupersaveContract.Budgets.INITIAL_AMOUNT, initialAmount.getAmount());
        cv.put(SupersaveContract.Budgets.USED_AMOUNT, totalExpense.getAmount());
        cv.put(SupersaveContract.Budgets.START_DATE, startDate.getTime());
        cv.put(SupersaveContract.Budgets.INITIAL_AMOUNT, endDate.getTime());
        return cv;
    }
}
