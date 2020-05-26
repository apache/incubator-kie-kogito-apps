package org.kie.kogito.trusty.storage.infinispan;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class InfinispanQueryFactoryTest {

    @Test
    public void GivenAnEmptyQuery_WhenQueryFactoryIsCalled_ThenTheEmptyQueryIsReturned() {
        Assertions.assertEquals("from test b", InfinispanQueryFactory.build(new TrustyStorageQuery(), "test"));
    }

    @Test
    public void GivenAQueryWithIntegerConditions_WhenQueryFactoryIsCalled_ThenInfinispanQueryIsBuiltProperly() {
        String expectedResult = "from test b where b.id=0 and b.id-1<=1 and b.id-2>=2";
        TrustyStorageQuery qq = new TrustyStorageQuery().where("id", IntegerOperator.EQUALS, 0).where("id-1", IntegerOperator.LTE, 1).where("id-2", IntegerOperator.GTE, 2);
        Assertions.assertEquals(expectedResult, InfinispanQueryFactory.build(qq, "test"));
    }

    @Test
    public void GivenAQueryWithStringConditions_WhenQueryFactoryIsCalled_ThenInfinispanQueryIsBuiltProperly() {
        String expectedResult = "from test b where b.id=\"myId\" and b.id-1:\"^myId2*\"";
        TrustyStorageQuery qq = new TrustyStorageQuery().where("id", StringOperator.EQUALS, "myId").where("id-1", StringOperator.PREFIX, "myId2");
        Assertions.assertEquals(expectedResult, InfinispanQueryFactory.build(qq, "test"));
    }

    @Test
    public void GivenAQueryWithMultipleConditionsTypes_WhenQueryFactoryIsCalled_ThenInfinispanQueryIsBuiltProperly() {
        String expectedResult = "from test b where b.id-1<=1 and b.id=\"myId\"";
        TrustyStorageQuery qq = new TrustyStorageQuery().where("id", StringOperator.EQUALS, "myId").where("id-1", IntegerOperator.LTE, 1);
        Assertions.assertEquals(expectedResult, InfinispanQueryFactory.build(qq, "test"));
    }
}
