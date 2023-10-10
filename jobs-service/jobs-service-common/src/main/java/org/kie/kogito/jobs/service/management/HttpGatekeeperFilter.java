package org.kie.kogito.jobs.service.management;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class HttpGatekeeperFilter {

    public static final String ERROR_MESSAGE = "Job Service instance is not master";
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @ConfigProperty(name = "quarkus.smallrye-health.root-path", defaultValue = "/q/health")
    private String healthCheckPath;

    protected void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        this.enabled.set(event.isEnabled());
    }

    @RouteFilter(100)
    void masterFilter(RoutingContext rc) throws Exception {
        if (!enabled.get() && !rc.request().path().contains(healthCheckPath)) {
            //block
            rc.response().setStatusCode(503);
            rc.response().setStatusMessage(ERROR_MESSAGE);
            rc.end();
            return;
        }
        //continue
        rc.next();
    }
}
