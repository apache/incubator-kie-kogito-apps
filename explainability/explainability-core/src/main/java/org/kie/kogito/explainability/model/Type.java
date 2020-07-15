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
package org.kie.kogito.explainability.model;

/**
 * Allowed data types
 */
public enum Type {

    TEXT("text"),

    CATEGORICAL("categorical"),

    BINARY("binary"),

    NUMBER("number"),

    BOOLEAN("boolean"),

    URI("uri"),

    TIME("time"),

    DURATION("duration"),

    VECTOR("vector"),

    UNDEFINED("undefined"),

    COMPOSITE("composite"),

    CURRENCY("currency");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Type fromValue(String text) {
        for (Type b : Type.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}