package org.kie.kogito.jobs.service.model;

import org.kie.kogito.jobs.service.api.PayloadData;

public interface Recipient {

    <T extends PayloadData> org.kie.kogito.jobs.service.api.Recipient<T> getRecipient();

}
