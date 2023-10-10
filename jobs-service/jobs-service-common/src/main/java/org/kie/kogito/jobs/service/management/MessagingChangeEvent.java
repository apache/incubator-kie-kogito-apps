package org.kie.kogito.jobs.service.management;

public class MessagingChangeEvent {

    private final boolean enabled;

    public MessagingChangeEvent(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
