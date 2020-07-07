/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.trusty.service.tracing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.TrustyService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceEventConsumerTest {

    @Test
    public void testCorrectCloudEvent() throws IOException {
        String payload = IOUtils.resourceToString("/TraceEventTest_correct_CloudEvent.json", StandardCharsets.UTF_8);
        doTest(payload, 1);
    }

    @Test
    public void testCloudEventWithoutData() throws IOException {
        String payload = IOUtils.resourceToString("/TraceEventTest_withoutData_CloudEvent.json", StandardCharsets.UTF_8);
        doTest(payload, 0);
    }

    @Test
    public void testGibberishPayload() throws IOException {
        String payload = "DefinitelyNotASerializedCloudEvent123456";
        doTest(payload, 0);
    }

    private void doTest(String payload, int wantedNumberOfServiceAndConverterInvocations) {
        TraceEventConverter converter = mock(TraceEventConverter.class);
        TrustyService service = mock(TrustyService.class);

        TraceEventConsumer consumer = new TraceEventConsumer(converter, service);

        Message<String> message = mock(Message.class);
        when(message.getPayload()).thenReturn(payload);

        consumer.handleMessage(message);

        verify(converter, times(wantedNumberOfServiceAndConverterInvocations)).toDecision(any());
        verify(service, times(wantedNumberOfServiceAndConverterInvocations)).storeDecision(any(), any());
        verify(message, times(1)).ack();
    }

}
