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
package org.kie.kogito.index.api.impl;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.kie.kogito.index.api.DataIndexServiceClient;
import org.kie.kogito.index.api.DataIndexServiceClientConfig;
import org.kie.kogito.index.api.DataIndexServiceClientFactory;
import org.kie.kogito.index.api.mp.GraphQLServiceClientMP;
import org.kie.kogito.index.api.mp.GraphQLServiceClientRest;

@ApplicationScoped
public class DataIndexServiceClientFactoryImpl implements DataIndexServiceClientFactory {

    @Override
    public DataIndexServiceClient newClient(DataIndexServiceClientConfig config) {
        return new DataIndexServiceClientImpl(new GraphQLServiceClientMP(RestClientBuilder.newBuilder()
                .baseUrl(config.getServiceUrl())
                .connectTimeout(config.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                .build(GraphQLServiceClientRest.class)));
    }
}
