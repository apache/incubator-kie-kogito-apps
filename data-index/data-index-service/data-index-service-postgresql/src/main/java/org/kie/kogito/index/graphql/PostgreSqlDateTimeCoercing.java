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

import java.time.ZonedDateTime;

import javax.enterprise.context.ApplicationScoped;

import graphql.language.StringValue;

@ApplicationScoped
public class PostgreSqlDateTimeCoercing extends DefaultDateTimeCoercing implements DateTimeCoercing {

    @Override
    public Object parseValue(Object input) {
        return input == null ? null : getZonedDateTime((String) input);
    }

    private ZonedDateTime getZonedDateTime(String input) {
        return parseDateTime(input);
    }

    @Override
    public Object parseLiteral(Object input) {
        if (input instanceof StringValue) {
            return getZonedDateTime(((StringValue) input).getValue());
        } else {
            return null;
        }
    }
}
