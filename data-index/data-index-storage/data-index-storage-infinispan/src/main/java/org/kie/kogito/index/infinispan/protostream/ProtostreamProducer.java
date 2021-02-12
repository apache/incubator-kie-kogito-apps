/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.infinispan.protostream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.MessageMarshaller;

@ApplicationScoped
public class ProtostreamProducer {

    @Inject
    ObjectMapper mapper;

    @Produces
    MessageMarshaller userTaskInstanceMarshaller() {
        return new UserTaskInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller processInstanceMarshaller() {
        return new ProcessInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller nodeInstanceMarshaller() {
        return new NodeInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller jobMarshaller() {
        return new JobMarshaller(mapper);
    }

    @Produces
    MessageMarshaller processInstanceErrorMarshaller() {
        return new ProcessInstanceErrorMarshaller(mapper);
    }

    @Produces
    MessageMarshaller milestoneMarshaller() {
        return new MilestoneMarshaller(mapper);
    }
}
