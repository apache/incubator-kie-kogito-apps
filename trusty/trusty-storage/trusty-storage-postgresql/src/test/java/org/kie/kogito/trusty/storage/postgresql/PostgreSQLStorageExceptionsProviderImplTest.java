package org.kie.kogito.trusty.storage.postgresql;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostgreSQLStorageExceptionsProviderImplTest {

    private static PostgreSQLStorageExceptionsProviderImpl provider;

    @BeforeAll
    public static void setup() {
        provider = new PostgreSQLStorageExceptionsProviderImpl();
    }

    @Test
    void testHibernateException() {
        assertFalse(provider.isConnectionException(new HibernateException("error")));
    }

    @Test
    void testHibernateConnectionException() {
        assertTrue(provider.isConnectionException(new JDBCConnectionException("error", new SQLException(""))));
    }
}
