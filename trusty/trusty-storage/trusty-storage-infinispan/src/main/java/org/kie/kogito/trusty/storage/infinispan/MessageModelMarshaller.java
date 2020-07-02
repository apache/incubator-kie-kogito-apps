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

    public static final String CATEGORY_FIELD = "category";
    public static final String EXCEPTION_FIELD = "exception";
    public static final String LEVEL_FIELD = "level";
    public static final String SOURCE_ID_FIELD = "sourceId";
    public static final String TEXT_FIELD = "text";
    public static final String TYPE_FIELD = "type";

    public MessageModelMarshaller(ObjectMapper mapper) {
        super(mapper, Message.class);
    }

    @Override
    public Message readFrom(ProtoStreamReader reader) throws IOException {
        return new Message(
                enumFromString(reader.readString(LEVEL_FIELD), Message.Level.class),
                reader.readString(CATEGORY_FIELD),
                reader.readString(TYPE_FIELD),
                reader.readString(SOURCE_ID_FIELD),
                reader.readString(TEXT_FIELD),
                reader.readObject(EXCEPTION_FIELD, MessageExceptionField.class)
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Message input) throws IOException {
        writer.writeString(LEVEL_FIELD, stringFromEnum(input.getLevel()));
        writer.writeString(CATEGORY_FIELD, input.getCategory());
        writer.writeString(TYPE_FIELD, input.getType());
        writer.writeString(SOURCE_ID_FIELD, input.getSourceId());
        writer.writeString(TEXT_FIELD, input.getText());
        writer.writeObject(EXCEPTION_FIELD, input.getException(), MessageExceptionField.class);
    }
}
