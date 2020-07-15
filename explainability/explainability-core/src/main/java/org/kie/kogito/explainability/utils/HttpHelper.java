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
package org.kie.kogito.explainability.utils;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to connect to a remote endpoint.
 */
public class HttpHelper {

    private static final CloseableHttpClient httpclient = createHttpClient();

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private ObjectMapper mapper = new ObjectMapper();

    private String baseHost;

    public HttpHelper(String baseHost) {
        this.baseHost = baseHost;
    }

    private static CloseableHttpClient createHttpClient() {
        int timeout = 60;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public String doGet(String path) throws IOException {
        HttpGet request = new HttpGet(baseHost + path);
        HttpResponse response = null;
        response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOGGER.debug("Get request returned " + result);
            return result;
        }

        return null;
    }

    public String doPost(String path, String params) throws IOException {

        HttpPost post = new HttpPost(baseHost + path);
        LOGGER.debug("Going to post to: " + path + "\n with: " + params);
        post.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpclient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        LOGGER.debug("I've got " + result);
        return result;
    }
}
