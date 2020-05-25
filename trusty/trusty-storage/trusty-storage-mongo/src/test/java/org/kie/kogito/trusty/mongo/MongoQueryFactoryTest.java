package org.kie.kogito.trusty.mongo;

import com.mongodb.client.model.Filters;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.BsonDocument;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;
import org.kie.kogito.trusty.storage.mongo.MongoQueryFactory;

@QuarkusTest
public class MongoQueryFactoryTest {

    @Test
    public void GivenATrustyQueryWithStrings_WhenMongoQueryFactoryIsCalled_ThenTheQueryIsBuilt() {
        Bson expected = Filters.and(Filters.eq("id", "testId"), Filters.regex("id-1", "^prefixId*"));

        TrustyStorageQuery q = new TrustyStorageQuery();
        q.where("id", StringOperator.EQUALS, "testId");
        q.where("id-1", StringOperator.PREFIX, "prefixId");

        BsonDocument bsonDocument = MongoQueryFactory.build(q).toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));
        BsonDocument bsonDocumentExpected = expected.toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));

        Assertions.assertEquals(bsonDocument.toJson(), bsonDocumentExpected.toJson());
    }

    @Test
    public void GivenATrustyQueryWithIntegers_WhenMongoQueryFactoryIsCalled_ThenTheQueryIsBuilt() {
        Bson expected = Filters.and(Filters.eq("id", 0), Filters.gte("id-1", 1), Filters.lte("id-2", 2));

        TrustyStorageQuery q = new TrustyStorageQuery();
        q.where("id", IntegerOperator.EQUALS, 0);
        q.where("id-1", IntegerOperator.GTE, 1);
        q.where("id-2", IntegerOperator.LTE, 2);

        BsonDocument bsonDocument = MongoQueryFactory.build(q).toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));
        BsonDocument bsonDocumentExpected = expected.toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));

        Assertions.assertEquals(bsonDocument.toJson(), bsonDocumentExpected.toJson());
    }

    @Test
    public void GivenATrustyQueryWithDates_WhenMongoQueryFactoryIsCalled_ThenTheQueryIsBuilt() {
        Bson expected = Filters.and(Filters.gte("id", "2020-01-01"), Filters.lte("id-1", "2020-01-02"));

        TrustyStorageQuery q = new TrustyStorageQuery();
        q.where("id", DateOperator.GTE, "2020-01-01");
        q.where("id-1", DateOperator.LTE, "2020-01-02");

        BsonDocument bsonDocument = MongoQueryFactory.build(q).toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));
        BsonDocument bsonDocumentExpected = expected.toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));

        Assertions.assertEquals(bsonDocument.toJson(), bsonDocumentExpected.toJson());
    }

    @Test
    public void GivenATrustyQuery_WhenMongoQueryFactoryIsCalled_ThenTheQueryIsBuilt() {
        Bson expected = Filters.and(Filters.eq("id-1", 0), Filters.eq("id-2", "Hello"), Filters.gte("id", "2020-01-01"));

        TrustyStorageQuery q = new TrustyStorageQuery();
        q.where("id", DateOperator.GTE, "2020-01-01");
        q.where("id-1", IntegerOperator.EQUALS, 0);
        q.where("id-2", StringOperator.EQUALS, "Hello");

        BsonDocument bsonDocument = MongoQueryFactory.build(q).toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));
        BsonDocument bsonDocumentExpected = expected.toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));

        Assertions.assertEquals(bsonDocument.toJson(), bsonDocumentExpected.toJson());
    }
}
