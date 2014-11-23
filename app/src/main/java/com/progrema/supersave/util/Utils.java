package com.progrema.supersave.util;

import java.util.Calendar;
import java.util.Date;

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
}
