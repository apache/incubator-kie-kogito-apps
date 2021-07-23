/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.quarkus;

import java.util.Map;

import org.kie.kogito.index.resources.KogitoServiceRandomPortTestResource;
import org.kie.kogito.resources.ConditionalQuarkusTestResource;

import static java.util.Collections.singletonMap;

public class KogitoServiceRandomPortQuarkusTestResource extends ConditionalQuarkusTestResource {

    public static final String QUARKUS_SERVICE_HTTP_PORT = "quarkus.http.test-port";

    public KogitoServiceRandomPortQuarkusTestResource() {
        super(new KogitoServiceRandomPortTestResource());
    }

    /**
     * The Kogito Service must be run first to make the port available in the rest of services.
     */
    @Override
    public int order() {
        return -1;
    }

    @Override
    protected Map<String, String> getProperties() {
        return singletonMap(QUARKUS_SERVICE_HTTP_PORT, String.valueOf(getTestResource().getMappedPort()));
    }
}
