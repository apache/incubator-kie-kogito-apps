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
package org.kie.kogito.taskassigning.auth.mp;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.taskassigning.auth.NoAuthenticationCredentials;

@ApplicationScoped
public class NoAuthenticationFilterProvider implements AuthenticationFilterProvider<NoAuthenticationCredentials> {

    public NoAuthenticationFilterProvider() {
        //CDI proxying
    }

    @Override
    public Class<NoAuthenticationCredentials> getCredentialsType() {
        return NoAuthenticationCredentials.class;
    }

    @Override
    public AuthenticationFilter createInstance(NoAuthenticationCredentials credentials) {
        return NoAuthenticationFilter.INSTANCE;
    }
}
