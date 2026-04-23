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
package org.kie.kogito.jobs.service.management;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class HttpGatekeeperFilter {

    public static final String ERROR_MESSAGE = "Job Service instance is not master";
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @ConfigProperty(name = "quarkus.smallrye-health.root-path", defaultValue = "/q/health")
    private String healthCheckPath;

    @ConfigProperty(name = "kogito.jobs-service.management.http.protected-paths", defaultValue = "/jobs,/v2/jobs")
    private List<String> protectedPaths;

    @ConfigProperty(name = "kogito.jobs-service.management.http.whitelist", defaultValue = "/management")
    private List<String> whitelistedPaths;

    protected void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        this.enabled.set(event.isEnabled());
    }

    @RouteFilter(100)
    void masterFilter(RoutingContext rc) throws Exception {
        String path = rc.request().path();
        if (!enabled.get() && isProtected(path) && !isWhitelisted(path)) {
            //block
            rc.response().setStatusCode(503);
            rc.response().setStatusMessage(ERROR_MESSAGE);
            rc.end();
            return;
        }
        //continue
        rc.next();
    }

    private boolean isProtected(String path) {
        return pathMatchesAny(path, protectedPaths);
    }

    private boolean isWhitelisted(String path) {
        return pathMatches(path, healthCheckPath) || pathMatchesAny(path, whitelistedPaths);
    }

    private boolean pathMatchesAny(String path, List<String> pathPrefixes) {
        return pathPrefixes != null && pathPrefixes.stream().anyMatch(prefix -> pathMatches(path, prefix));
    }

    private boolean pathMatches(String path, String pathPrefix) {
        if (path == null || pathPrefix == null || pathPrefix.isBlank()) {
            return false;
        }
        String normalizedPrefix = normalizePath(pathPrefix);
        return "/".equals(normalizedPrefix) || path.equals(normalizedPrefix) || path.startsWith(normalizedPrefix + "/");
    }

    private String normalizePath(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.length() > 1 && normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }
}
