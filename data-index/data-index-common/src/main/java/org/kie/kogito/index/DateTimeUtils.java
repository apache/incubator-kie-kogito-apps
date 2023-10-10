package org.kie.kogito.index;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String formatDateTime(Date time) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time.toInstant().atZone(ZoneOffset.UTC));
    }

    public static String formatOffsetDateTime(OffsetDateTime time) {
        return time.truncatedTo(ChronoUnit.MILLIS).atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String formatZonedDateTime(ZonedDateTime time) {
        return time.truncatedTo(ChronoUnit.MILLIS).withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        return date == null ? null : ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

    public static ZonedDateTime toZonedDateTime(OffsetDateTime date) {
        return date == null ? null : date.atZoneSameInstant(ZoneOffset.UTC);
    }

    public static ZonedDateTime parseZonedDateTime(String s) {
        try {
            return ZonedDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException exception) {
            return LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneOffset.UTC);
        }
    }
}
