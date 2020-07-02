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
import org.kie.kogito.trusty.storage.api.model.MessageExceptionField;

public class MessageExceptionFieldModelMarshaller extends AbstractModelMarshaller<MessageExceptionField> {

    public static final String CAUSE_FIELD = "cause";
    public static final String CLASS_NAME_FIELD = "className";
    public static final String MESSAGE_FIELD = "message";

    public MessageExceptionFieldModelMarshaller(ObjectMapper mapper) {
        super(mapper, MessageExceptionField.class);
    }

    @Override
    public MessageExceptionField readFrom(ProtoStreamReader reader) throws IOException {
        return new MessageExceptionField(
                reader.readString(CLASS_NAME_FIELD),
                reader.readString(MESSAGE_FIELD),
                reader.readObject(CAUSE_FIELD, MessageExceptionField.class)
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, MessageExceptionField input) throws IOException {
        writer.writeString(CLASS_NAME_FIELD, input.getClassName());
        writer.writeString(MESSAGE_FIELD, input.getMessage());
        writer.writeObject(CAUSE_FIELD, input.getCause(), MessageExceptionField.class);
    }
}
