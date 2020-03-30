/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.mongodb.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelUtils.class);

    private static final JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString())).build();

    public static ZonedDateTime instantToZonedDateTime(Long milli) {
        return Optional.ofNullable(milli).map(time -> Instant.ofEpochMilli(time).atZone(ZoneOffset.UTC)).orElse(null);
    }

    public static Long zonedDateTimeToInstant(ZonedDateTime time) {
        return Optional.ofNullable(time).map(t -> t.toInstant().toEpochMilli()).orElse(null);
    }

    public static <T extends JsonNode> T dbObjectToJsonNode(BasicDBObject dbObject, Class<T> type) {
        return Optional.ofNullable(dbObject).map(obj -> {
            try {
                return JsonUtils.getObjectMapper().readValue(obj.toJson(jsonWriterSettings), type);
            } catch (JsonProcessingException ex) {
                LOGGER.error("Error trying to parse Process Variables", ex);
                return null;
            }
        }).orElse(null);
    }

    public static BasicDBObject jsonNodeToDBObject(JsonNode jsonNode) {
        return Optional.ofNullable(jsonNode).map(json -> BasicDBObject.parse(json.toString())).orElse(null);
    }
}
