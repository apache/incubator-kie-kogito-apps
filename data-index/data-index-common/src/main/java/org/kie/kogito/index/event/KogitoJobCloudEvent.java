package org.kie.kogito.index.event;

import java.net.URI;

import org.kie.kogito.index.model.Job;

public class KogitoJobCloudEvent extends KogitoCloudEvent<Job> {

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void setSource(URI source) {
        super.setSource(source);
        if (getData() != null && source != null) {
            getData().setEndpoint(source.toString());
        }
    }

    @Override
    public void setData(Job data) {
        super.setData(data);
        setSource(getSource());
    }

    @Override
    public String toString() {
        return "KogitoJobCloudEvent{} " + super.toString();
    }

    public static final class Builder extends AbstractBuilder<Builder, Job, KogitoJobCloudEvent> {

        private Builder() {
            super(new KogitoJobCloudEvent());
        }
    }
}
