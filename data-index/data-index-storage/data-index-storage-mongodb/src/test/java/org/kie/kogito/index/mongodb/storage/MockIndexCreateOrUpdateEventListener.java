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

package org.kie.kogito.index.mongodb.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;

@ApplicationScoped
public class MockIndexCreateOrUpdateEventListener {

    private boolean triggered;

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public void onIndexCreateOrUpdateEvent(@Observes IndexCreateOrUpdateEvent event) {
        triggered = true;
    }
}
