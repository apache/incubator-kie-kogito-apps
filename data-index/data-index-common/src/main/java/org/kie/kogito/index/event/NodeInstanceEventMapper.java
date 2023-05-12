/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.event;

import java.util.function.Function;

import org.kie.kogito.event.process.NodeInstanceDataEvent;
import org.kie.kogito.event.process.NodeInstanceEventBody;
import org.kie.kogito.index.model.NodeInstance;

import static org.kie.kogito.index.DateTimeUtils.toZonedDateTime;

public class NodeInstanceEventMapper implements Function<NodeInstanceDataEvent, NodeInstance> {

    @Override
    public NodeInstance apply(NodeInstanceDataEvent event) {
        if (event == null || event.getData() == null) {
            return null;
        }

        NodeInstanceEventBody nib = event.getData();
        NodeInstance ni = new NodeInstance();
        ni.setProcessInstanceId(event.getKogitoProcessInstanceId());
        ni.setId(nib.getId());

        switch (event.getData().getEventType()) {
            case 1:
                ni.setEnter(toZonedDateTime(nib.getEventTime()));
                break;
            case 2:
                ni.setExit(toZonedDateTime(nib.getEventTime()));
                break;
        }

        ni.setName(nib.getNodeName());
        ni.setType(nib.getNodeType());
        ni.setNodeId(nib.getNodeId());
        ni.setDefinitionId(nib.getNodeDefinitionId());
        return ni;

    }

}
