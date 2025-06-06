/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.jitexecutor.dmn.responses;

import java.io.Serializable;
import java.util.Objects;

import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.internal.builder.InternalMessage;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JITDMNMessage implements Serializable, DMNMessage {

    public static enum DMNMessageSeverityKS {
        INFO,
        WARN,
        ERROR;

        public static DMNMessageSeverityKS of(Severity value) {
            switch (value) {
                case ERROR:
                    return DMNMessageSeverityKS.ERROR;
                case INFO:
                    return DMNMessageSeverityKS.INFO;
                case TRACE:
                    return DMNMessageSeverityKS.INFO;
                case WARN:
                    return DMNMessageSeverityKS.WARN;
                default:
                    return DMNMessageSeverityKS.ERROR;
            }
        }

        public Severity asSeverity() {
            switch (this) {
                case ERROR:
                    return Severity.ERROR;
                case INFO:
                    return Severity.INFO;
                case WARN:
                    return Severity.WARN;
                default:
                    return Severity.ERROR;
            }
        }
    }

    private DMNMessageSeverityKS severity;
    private String message;
    private DMNMessageType messageType;
    private String sourceId;
    private String path;

    public JITDMNMessage() {
        // Intentionally blank.
    }

    public static JITDMNMessage of(DMNMessage value) {
        JITDMNMessage res = new JITDMNMessage();
        res.severity = DMNMessageSeverityKS.of(value.getSeverity());
        res.message = value.getMessage();
        res.messageType = value.getMessageType();
        res.sourceId = value.getSourceId();
        res.path = value.getPath();
        return res;
    }

    @Override
    public Severity getSeverity() {
        return severity.asSeverity();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public DMNMessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @JsonIgnore
    @Override
    public Throwable getException() {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public FEELEvent getFeelEvent() {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public Object getSourceReference() {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public String getKieBaseName() {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public InternalMessage setKieBaseName(String kieBaseName) {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @JsonIgnore
    @Override
    public int getLine() {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public int getColumn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Level getLevel() {
        switch (severity) {
            case ERROR:
                return Level.ERROR;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARNING;
            default:
                return Level.ERROR;
        }
    }

    @JsonIgnore
    @Override
    public String getText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JITDMNMessage that)) {
            return false;
        }
        return severity == that.severity && Objects.equals(message, that.message) && messageType == that.messageType && Objects.equals(sourceId, that.sourceId) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, message, messageType, sourceId, path);
    }

    @Override
    public String toString() {
        return "JITDMNMessage{" +
                "severity=" + severity +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                ", sourceId='" + sourceId + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}