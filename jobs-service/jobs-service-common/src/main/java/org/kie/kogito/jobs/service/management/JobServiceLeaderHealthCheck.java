package org.kie.kogito.jobs.service.management;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class JobServiceLeaderHealthCheck implements HealthCheck {

    private AtomicBoolean enabled = new AtomicBoolean(false);

    protected void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        this.enabled.set(event.isEnabled());
    }

    @Override
    public HealthCheckResponse call() {
        final HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Leader Instance");
        if (enabled.get()) {
            return responseBuilder.up().build();
        }
        return responseBuilder.down().build();
    }
}