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
package org.kie.kogito.jobs.service.runtime;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class HttpGatekeeperFilter {

    private AtomicBoolean enabled = new AtomicBoolean(false);

    protected void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        this.enabled.set(event.isEnabled());
    }

    @RouteFilter(100)
    void masterFilter(RoutingContext rc) {
        if (!enabled.get() && !rc.request().path().contains("q/health")) {
            //block
            rc.response().setStatusCode(500);
            rc.end("Not master");
            return;
        }
        //continue
        rc.next();
    }
}
