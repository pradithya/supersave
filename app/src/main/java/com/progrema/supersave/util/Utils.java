package com.progrema.supersave.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Dell on 11/23/2014.
 */
public class Utils {

    private final static long SECOND_MILLIS = 1000;
    private final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    private final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    private final static long DAY_MILLIS = HOUR_MILLIS * 24;

    public static int getDayDiff(int start, int end){
        int diff = 0;

        long startMillis = Calendar.getInstance().getTimeInMillis();
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.DATE, end);

        if(start > end) {
            endDate.add(Calendar.MONTH,1);
        }

        long endMillis = endDate.getTimeInMillis();
        diff = (int)((endMillis - startMillis)/DAY_MILLIS);

        return diff;
    }

    public static List<String> getAllCurrenciesCode()
    {
        Set<String> toret = new HashSet<>();

        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                toret.add( Currency.getInstance( loc ).getCurrencyCode() );
            } catch(Exception exc)
            {
                // Locale not found
            }
        }
        List<String> currencyCode = new ArrayList<String>(toret);
        Collections.sort(currencyCode);
        return currencyCode;
    }
}
