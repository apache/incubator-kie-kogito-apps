/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.taskassigning.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class AbstractTaskAssigningTest {

    /**
     * System property for triggering the "turtle tests". This tests are part of the product but only executed when
     * this system property is set. It's up to the productization scripts to determine when this tests should be
     * executed or not. Developers can always trigger them locally if needed.
     */
    String RUN_TURTLE_TESTS = "runTurtleTests";

    /**
     * System property for triggering the "development only tests".
     * This tests are not intended be part of product the build tests and should not be part of the productization scripts,
     * since they are only useful for developers to test stuff locally during development (e.g. for executing random operations)
     * Don't abuse with the use this tests.
     */
    String RUN_DEVELOPMENT_ONLY_TESTS = "runDevelopmentOnlyTests";

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected void checkRunTurtleTests() {
        assumeTrue("true".equals(System.getProperty(RUN_TURTLE_TESTS)),
                   "This test was intentionally disabled, it can be enabled by passing the JVM parameter -DrunTurtleTests=true");
    }

    protected void checkRunDevelopmentOnlyTests() {
        assumeTrue("true".equals(System.getProperty(RUN_DEVELOPMENT_ONLY_TESTS)),
                   "This test was intentionally disabled, it can be enabled by passing the JVM parameter -runDevelopmentOnlyTests=true");
    }
}
