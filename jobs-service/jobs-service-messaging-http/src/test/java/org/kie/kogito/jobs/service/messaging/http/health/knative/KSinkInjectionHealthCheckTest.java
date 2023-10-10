package org.kie.kogito.jobs.service.messaging.http.health.knative;

import java.util.function.UnaryOperator;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.jobs.service.messaging.http.health.knative.KSinkInjectionHealthCheck.K_SINK;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class KSinkInjectionHealthCheckTest {

    private KSinkInjectionHealthCheck healthCheck;

    @Mock
    private UnaryOperator<String> envReader;

    @BeforeEach
    void setUp() {
        healthCheck = new KSinkInjectionHealthCheck(envReader);
    }

    @Test
    void callSuccessful() {
        doReturn("http://localhost:8080").when(envReader).apply(K_SINK);
        HealthCheckResponse response = healthCheck.call();
        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.UP);
    }

    @Test
    void callUnsuccessfulMissingEnvVar() {
        HealthCheckResponse response = healthCheck.call();
        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
    }

    @Test
    void callUnsuccessfulBadURL() {
        doReturn("that's not a url").when(envReader).apply(K_SINK);
        HealthCheckResponse response = healthCheck.call();
        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
    }

    @Test
    void callUnsuccessfulUnresolvableURL() {
        // according with https://www.rfc-editor.org/rfc/rfc6761#section-6.4, domains with .invalid should never
        // be resolved.
        doReturn("http://something.invalid").when(envReader).apply(K_SINK);
        HealthCheckResponse response = healthCheck.call();
        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
    }
}
