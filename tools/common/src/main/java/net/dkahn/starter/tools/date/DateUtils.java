package net.dkahn.starter.tools.date;

import java.time.*;
import java.util.Date;

/**
 * Created by dev on 26/11/16.
 */
public class DateUtils {
    //can be overridden for testing purpose
    public static Clock clock = Clock.systemDefaultZone();

    public static Date asDate(LocalDate localDate) {
        if(localDate==null) return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        if(localDateTime==null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        if(date==null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        if(date==null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
