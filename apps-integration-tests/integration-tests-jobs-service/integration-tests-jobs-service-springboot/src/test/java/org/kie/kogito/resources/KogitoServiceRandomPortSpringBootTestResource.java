/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.resources;

import org.kie.kogito.it.KogitoServiceRandomPortTestResource;

public class KogitoServiceRandomPortSpringBootTestResource extends ConditionalSpringBootTestResource {

    public static final String SPRINGBOOT_SERVICE_HTTP_PORT = "server.port";

    public KogitoServiceRandomPortSpringBootTestResource() {
        super(new KogitoServiceRandomPortTestResource());
    }

    @Override
    protected String getKogitoProperty() {
        return SPRINGBOOT_SERVICE_HTTP_PORT;
    }

    @Override
    protected String getKogitoPropertyValue() {
        return String.valueOf(getTestResource().getMappedPort());
    }

}
