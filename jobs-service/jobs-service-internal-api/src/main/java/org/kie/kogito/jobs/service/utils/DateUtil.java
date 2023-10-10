package org.kie.kogito.jobs.service.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public class DateUtil {

    public static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    private DateUtil() {

    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(DEFAULT_ZONE).truncatedTo(ChronoUnit.MILLIS);
    }

    public static Instant zonedDateTimeToInstant(ZonedDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(t -> t.withZoneSameLocal(DEFAULT_ZONE))
                .map(t -> t.truncatedTo(ChronoUnit.MILLIS))
                .map(ZonedDateTime::toInstant)
                .orElse(null);
    }

    public static ZonedDateTime instantToZonedDateTime(Instant instant) {
        return Optional.ofNullable(instant)
                .map(i -> ZonedDateTime.ofInstant(i, DateUtil.DEFAULT_ZONE))
                .map(i -> i.truncatedTo(ChronoUnit.MILLIS))
                .orElse(null);
    }

    public static ZonedDateTime fromDate(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
    }

    public static Date toDate(OffsetDateTime dateTime) {
        return new Date(dateTime.toInstant().toEpochMilli());
    }

    public static OffsetDateTime dateToOffsetDateTime(Date date) {
        return fromDate(date).toOffsetDateTime();
    }
}
