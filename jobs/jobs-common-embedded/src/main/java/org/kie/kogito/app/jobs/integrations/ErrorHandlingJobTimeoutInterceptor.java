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
package org.kie.kogito.app.jobs.integrations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.kie.kogito.app.jobs.api.JobTimeoutInterceptor;
import org.kie.kogito.handler.ExceptionHandler;

public class ErrorHandlingJobTimeoutInterceptor implements JobTimeoutInterceptor {

    private List<ExceptionHandler> exceptionHandlers;

    public ErrorHandlingJobTimeoutInterceptor(List<ExceptionHandler> exceptionHandlers) {
        this.exceptionHandlers = new ArrayList<>(exceptionHandlers);
    }

    @Override
    public Integer priority() {
        return 50;
    }

    @Override
    public Callable<Void> chainIntercept(Callable<Void> callable) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    return callable.call();
                } catch (Exception ex) {
                    exceptionHandlers.stream().forEach(e -> e.handle(ex));
                    return null;
                }
            }
        };
    }

}
