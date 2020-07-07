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

package org.kie.kogito.trusty.service.responses;

import org.kie.kogito.trusty.storage.api.model.Message;

public class MessageResponse {

    private String level;
    private String category;
    private String type;
    private String sourceId;
    private String text;
    private MessageExceptionFieldResponse exception;

    private MessageResponse() {
    }

    public MessageResponse(String level, String category, String type, String sourceId, String text, MessageExceptionFieldResponse exception) {
        this.level = level;
        this.category = category;
        this.type = type;
        this.sourceId = sourceId;
        this.text = text;
        this.exception = exception;
    }

    public String getLevel() {
        return level;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getText() {
        return text;
    }

    public MessageExceptionFieldResponse getException() {
        return exception;
    }

    public static MessageResponse from(Message message) {
        return message == null ? null : new MessageResponse(
                message.getLevel() == null ? null : message.getLevel().name(),
                message.getCategory(),
                message.getType(),
                message.getSourceId(),
                message.getText(),
                MessageExceptionFieldResponse.from(message.getException())
        );
    }
}
