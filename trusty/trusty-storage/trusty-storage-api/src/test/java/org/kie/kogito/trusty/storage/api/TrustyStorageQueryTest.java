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

package org.kie.kogito.trusty.storage.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class TrustyStorageQueryTest {

    @Test
    public void GivenACondition_WhenTrustyQueryIsUpdated_ThenTheConditionIsStored() {
        TrustyStorageQuery q = new TrustyStorageQuery();

        Assertions.assertEquals(0, q.getDateConditions().size());
        Assertions.assertEquals(0, q.getStringConditions().size());
        Assertions.assertEquals(0, q.getIntegerConditions().size());

        q.where("id-0", StringOperator.EQUALS, "test");

        Assertions.assertEquals(0, q.getDateConditions().size());
        Assertions.assertEquals(1, q.getStringConditions().size());
        Assertions.assertEquals(0, q.getIntegerConditions().size());

        q.where("id-1", StringOperator.EQUALS, "test");

        Assertions.assertEquals(0, q.getDateConditions().size());
        Assertions.assertEquals(2, q.getStringConditions().size());
        Assertions.assertEquals(0, q.getIntegerConditions().size());

        q.where("id-2", DateOperator.LTE, "2020-10-10");

        Assertions.assertEquals(1, q.getDateConditions().size());
        Assertions.assertEquals(2, q.getStringConditions().size());
        Assertions.assertEquals(0, q.getIntegerConditions().size());

        q.where("id-3", IntegerOperator.EQUALS, 0);

        Assertions.assertEquals(1, q.getDateConditions().size());
        Assertions.assertEquals(2, q.getStringConditions().size());
        Assertions.assertEquals(1, q.getIntegerConditions().size());
    }
}
