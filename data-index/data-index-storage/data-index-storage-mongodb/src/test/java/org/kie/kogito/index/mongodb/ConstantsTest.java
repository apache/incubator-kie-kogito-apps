package org.kie.kogito.index.mongodb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantsTest {

    @Test
    void testGetDomainCollectionName() {
        assertEquals("test_domain", Constants.getDomainCollectionName("test"));
    }

    @Test
    void testIsDomainCollection() {
        assertTrue(Constants.isDomainCollection("test_domain"));
        assertFalse(Constants.isDomainCollection("test_domain1"));
    }
}