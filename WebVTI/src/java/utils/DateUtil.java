/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author soft
 */
public class DateUtil {
    
    public static Calendar getCalendarInstance() {
        return Calendar.getInstance(TimeZone.getTimeZone("Europe/Brussels"));
    }
    
    public static Date today() {
        Calendar cal = getCalendarInstance();
        return cal.getTime();
    }
    
    /**
     * create a new date with the specified data, the hours and minutes will be initialized with 00:00:00.
     * @param year
     * @param month
     * @param day
     * @return 
     */
    public static Date date(int year, int month, int day) {
        Calendar cal = getCalendarInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
