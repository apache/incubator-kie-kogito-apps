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
package org.acme.travels.service;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Simple string manipulation service for testing recordArgs feature.
 * Used in node-level-false.bpmn, mixed-nodes.bpmn, and no-recordargs.bpmn processes.
 */
@ApplicationScoped
public class StringService {

    public String toUpperCase(String input) {
        return input != null ? input.toUpperCase() : null;
    }

    public String toLowerCase(String input) {
        return input != null ? input.toLowerCase() : null;
    }

    public String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }

    public String concatenate(String str1, String str2) {
        return (str1 != null ? str1 : "") + (str2 != null ? str2 : "");
    }

    public StringResult process(String text, String operation) {
        StringResult result = new StringResult();
        result.setOriginal(text);
        result.setOperation(operation);

        if (text == null) {
            result.setProcessed(null);
            return result;
        }

        switch (operation.toLowerCase()) {
            case "uppercase":
                result.setProcessed(toUpperCase(text));
                break;
            case "lowercase":
                result.setProcessed(toLowerCase(text));
                break;
            case "reverse":
                result.setProcessed(reverse(text));
                break;
            case "length":
                result.setProcessed(String.valueOf(text.length()));
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }

        return result;
    }

    public static class StringResult {
        private String original;
        private String operation;
        private String processed;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getProcessed() {
            return processed;
        }

        public void setProcessed(String processed) {
            this.processed = processed;
        }
    }
}

// Made with Bob
