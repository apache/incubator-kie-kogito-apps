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

package org.kie.kogito.persistence.mongodb.index;

import java.util.Objects;

import org.kie.kogito.persistence.api.schema.ProcessDescriptor;

public class ProcessIndexEvent {

   ProcessDescriptor processDescriptor;

    public ProcessIndexEvent(ProcessDescriptor processDescriptor) {
        this.processDescriptor = processDescriptor;
    }

    public ProcessDescriptor getProcessDescriptor() {
        return processDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessIndexEvent that = (ProcessIndexEvent) o;
        return Objects.equals(processDescriptor, that.processDescriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processDescriptor);
    }
}
