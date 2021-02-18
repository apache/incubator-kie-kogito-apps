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

package org.kie.kogito.trusty.service.common.messaging.incoming;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelIdCreatorTest {

    private static Stream<Arguments> provideParametersForModelIdCreator() {
        return Stream.of(
                Arguments.of(null, null, null, "name", "namespace", "name:namespace"),
                Arguments.of("ignore", null, null, "name", "namespace", "name:namespace"),
                Arguments.of(null, "ignore", null, "name", "namespace", "name:namespace"),
                Arguments.of(null, null, "ignore", "name", "namespace", "name:namespace"),
                Arguments.of(null, null, null, "name", null, "name:"),
                Arguments.of(null, null, null, null, "namespace", ":namespace")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForModelIdCreator")
    void isBlank_ShouldReturnTrueForNullOrBlankStrings(final String groupId,
                                                       final String artifactId,
                                                       final String version,
                                                       final String name,
                                                       final String namespace,
                                                       final String expected) {
        assertEquals(expected, ModelIdCreator.makeIdentifier(groupId,
                                                             artifactId,
                                                             version,
                                                             name,
                                                             namespace));
    }
}
