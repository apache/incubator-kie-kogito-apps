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

package org.kie.kogito.trusty.service.responses;

import org.kie.kogito.trusty.service.models.ExecutionTypeEnum;

/**
 * The execution enum type.
 */
public enum ExecutionTypeEnumResponse {

    /**
     * An execution of a decision.
     */
    DECISION("DECISION"),

    /**
     * An execution of a process.
     */
    PROCESS("PROCESS");

    public String type;

    ExecutionTypeEnumResponse(String type) {
        this.type = type;
    }

    public static ExecutionTypeEnumResponse from(ExecutionTypeEnum executionType) {
        switch (executionType) {
            case PROCESS:
                return PROCESS;
            case DECISION:
                return DECISION;
            default:
                throw new IllegalArgumentException("Execution type not supported.");
        }
    }
}