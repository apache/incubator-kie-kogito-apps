/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.index.graphql;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import graphql.language.StringValue;
import graphql.schema.CoercingSerializeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class DefaultDateTimeCoercingTest {

    DefaultDateTimeCoercing dateTimeCoercing = new DefaultDateTimeCoercing();

    @Test
    public void testParseValueAsLong() {
        assertThat(dateTimeCoercing.parseValue(null)).isNull();
        assertThat(dateTimeCoercing.parseValue("2019-11-20T03:14:03.075Z"))
                .isEqualTo(ZonedDateTime.parse("2019-11-20T03:14:03.075Z").truncatedTo(ChronoUnit.MILLIS).toInstant()
                        .toEpochMilli());
    }

    @Test
    public void testParseLiteral() {
        assertThat(dateTimeCoercing.parseLiteral(null)).isNull();
        assertThat(dateTimeCoercing.parseLiteral(new StringValue("2019-11-20T03:14:03.075Z"))).isEqualTo(1574219643075l);
    }

    @Test
    public void testSerializeNull() {
        try {
            dateTimeCoercing.serialize(null);
            fail("Method should throw CoercingSerializeException");
        } catch (CoercingSerializeException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("Expected something we can convert to 'java.time.ZonedDateTime' but was 'null'.");
        }
    }

    @Test
    public void testSerializeInvalidType() {
        try {
            dateTimeCoercing.serialize(1);
            fail("Method should throw CoercingSerializeException");
        } catch (CoercingSerializeException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("Expected something we can convert to 'java.time.ZonedDateTime' but was 'java.lang.Integer'.");
        }
    }

    @Test
    public void testSerializeZonedDateTime() {
        ZonedDateTime time = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);
        String result = dateTimeCoercing.serialize(time);
        assertThat(result).isEqualTo(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time));
    }

    @Test
    public void testSerializeMillisAsString() {
        ZonedDateTime time = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);
        String result = dateTimeCoercing.serialize(String.valueOf(time.toInstant().toEpochMilli()));
        assertThat(result).isEqualTo(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Test
    public void testSerializeInvalidString() {
        try {
            dateTimeCoercing.serialize("test");
            fail("Method should throw CoercingSerializeException");
        } catch (CoercingSerializeException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("Invalid ISO-8601 value : 'test'. because of : 'Text 'test' could not be parsed at index 0'");
        }
    }

    @Test
    public void testSerializeString() {
        String result = dateTimeCoercing.serialize("2019-08-20T19:26:02.092+00:00");
        assertThat(result).isEqualTo("2019-08-20T19:26:02.092Z");
    }
}
