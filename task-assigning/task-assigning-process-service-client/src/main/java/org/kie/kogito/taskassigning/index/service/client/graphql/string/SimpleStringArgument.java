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
package org.kie.kogito.taskassigning.index.service.client.graphql.string;

import com.fasterxml.jackson.databind.JsonNode;

import static org.kie.kogito.taskassigning.util.JsonUtils.newObjectNode;

public abstract class SimpleStringArgument<T> extends StringArgument<T> {

    protected SimpleStringArgument(T value, Condition condition) {
        super(value, condition);
    }

    public abstract String getStringValue();

    @Override
    public JsonNode asJson() {
        return newObjectNode(getCondition().getFunction(), getStringValue());
    }
}
