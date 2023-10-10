package org.kie.kogito.jobs.service.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import io.quarkus.arc.DefaultBean;

@ApplicationScoped
public class InMemoryConfiguration {

    @DefaultBean
    @Produces
    @Readiness
    public HealthCheck inMemoryHealthCheck() {
        return () -> HealthCheckResponse.up("In Memory Persistence");
    }
}
