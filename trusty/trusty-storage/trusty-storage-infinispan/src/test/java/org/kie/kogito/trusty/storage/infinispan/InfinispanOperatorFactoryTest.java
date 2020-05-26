package org.kie.kogito.trusty.storage.infinispan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;

public class InfinispanOperatorFactoryTest {
    @Test
    public void GivenOperators_WhenOperatorFactoryIsCalled_ThenTheOperatorIsProperlyConverted(){
        Assertions.assertEquals("<=", InfinispanOperatorFactory.convert(IntegerOperator.LTE));
        Assertions.assertEquals(">=", InfinispanOperatorFactory.convert(IntegerOperator.GTE));
        Assertions.assertEquals("=", InfinispanOperatorFactory.convert(IntegerOperator.EQUALS));
        Assertions.assertEquals("<=", InfinispanOperatorFactory.convert(DateOperator.LTE));
        Assertions.assertEquals(">=", InfinispanOperatorFactory.convert(DateOperator.GTE));
    }
}
