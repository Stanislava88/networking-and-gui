package com.clouway.conversation.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public final class CalendarUtil {
    public static Date january(String name, int year, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Date(calendar.getTime().getTime()) {
            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static Date january(int year, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();

    }

    public static Date february(int year, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
