/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.job.sink.recipient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.kie.kogito.jobs.service.api.Recipient;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient;
import org.kie.kogito.jobs.service.api.utils.EventUtils;
import org.kie.kogito.jobs.service.validator.RecipientValidator;
import org.kie.kogito.jobs.service.validator.ValidationException;

@ApplicationScoped
public class SinkRecipientValidator implements RecipientValidator {

    @Override
    public boolean accept(Recipient<?> recipient) {
        return recipient instanceof SinkRecipient;
    }

    @Override
    public boolean validate(Recipient<?> recipient) {
        if (!(recipient instanceof SinkRecipient)) {
            throw new ValidationException("Recipient must be a non-null instance of: " + SinkRecipient.class);
        }
        SinkRecipient<?> sinkRecipient = (SinkRecipient<?>) recipient;
        if (StringUtils.isBlank(sinkRecipient.getSinkUrl())) {
            throw new ValidationException("SinkRecipient sinkUrl must have a non empty value.");
        }
        try {
            new URL(sinkRecipient.getSinkUrl());
        } catch (MalformedURLException e) {
            throw new ValidationException("SinkRecipient must have a valid url.", e);
        }
        if (Objects.isNull(sinkRecipient.getContentMode())) {
            throw new ValidationException("SinkRecipient contentMode must have a non null value.");
        }
        if (Objects.isNull(sinkRecipient.getCeSpecVersion())) {
            throw new ValidationException("SinkRecipient ce-specversion must have a non null value.");
        }
        if (StringUtils.isBlank(sinkRecipient.getCeType())) {
            throw new ValidationException("SinkRecipient ce-type must have a non empty value.");
        }
        if (Objects.isNull(sinkRecipient.getCeSource())) {
            throw new ValidationException("SinkRecipient ce-source must have a non null value.");
        }
        sinkRecipient.getCeExtensions().keySet()
                .forEach(EventUtils::validateExtensionName);

        return true;
    }
}