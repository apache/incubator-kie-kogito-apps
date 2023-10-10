package org.kie.kogito.jobs.service.utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    void dateToOffsetDateTime() {
        Date date = new Date();
        OffsetDateTime offsetDateTime = DateUtil.dateToOffsetDateTime(date);
        assertThat(offsetDateTime).isEqualTo(OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }
}