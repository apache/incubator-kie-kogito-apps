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
package org.kie.kogito.jobs.service.model;

import java.util.Objects;

import org.kie.kogito.jobs.service.api.RecipientDescriptorRegistry;

public class RecipientInstance implements Recipient {

    private final org.kie.kogito.jobs.service.api.Recipient<?> recipient;

    public RecipientInstance(org.kie.kogito.jobs.service.api.Recipient<?> recipient) {
        Objects.requireNonNull(recipient);
        this.recipient = recipient;
    }

    @Override
    public String type() {
        return RecipientDescriptorRegistry.getInstance()
                .getDescriptor(recipient).orElseThrow(() -> new IllegalStateException("Recipient Type not found"))
                .getName();
    }

    @Override
    public org.kie.kogito.jobs.service.api.Recipient<?> getRecipient() {
        return recipient;
    }
}