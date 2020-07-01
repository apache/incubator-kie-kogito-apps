/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.kogito.trusty.storage.api.model.Message;
import org.kie.kogito.trusty.storage.api.model.MessageExceptionField;

public class MessageModelMarshaller extends AbstractModelMarshaller<Message> {

    public MessageModelMarshaller(ObjectMapper mapper) {
        super(mapper, Message.class);
    }

    @Override
    public Message readFrom(ProtoStreamReader reader) throws IOException {
        return new Message(
                enumFromString(reader.readString("level"), Message.Level.class),
                reader.readString("category"),
                reader.readString("type"),
                reader.readString("sourceId"),
                reader.readString("text"),
                reader.readObject("exception", MessageExceptionField.class)
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Message input) throws IOException {
        writer.writeString("level", stringFromEnum(input.getLevel()));
        writer.writeString("category", input.getCategory());
        writer.writeString("type", input.getType());
        writer.writeString("sourceId", input.getSourceId());
        writer.writeString("text", input.getText());
        writer.writeObject("exception", input.getException(), MessageExceptionField.class);
    }
}
