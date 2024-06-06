/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import net.matrix.sql.ConnectionInfo;

import static org.assertj.core.api.Assertions.assertThat;

class SessionFactoryManagerTest {
    @Test
    void testGetInstance() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        assertThat(manager.getName()).isEqualTo(SessionFactoryManager.DEFAULT_NAME);

        SessionFactoryManager manager2 = SessionFactoryManager.getInstance(SessionFactoryManager.DEFAULT_NAME);
        assertThat(manager2).isSameAs(manager);
    }

    @Test
    void testGetInstance_name() {
        String name = "getInstance";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager manager = SessionFactoryManager.getInstance(name);
        assertThat(manager.getName()).isEqualTo(name);
    }

    @Test
    void testIsNamed() {
        String name = "isNamed";
        SessionFactoryManager.nameInstance(name);

        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    void testResetAll() {
        String name = "resetAll";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager.resetAll();
        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    void testClearAll() {
        String name = "clearAll";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager.clearAll();
        assertThat(SessionFactoryManager.isNamed(name)).isFalse();
    }

    @Test
    void testReset() {
        String name = "reset";
        SessionFactoryManager.nameInstance(name);
        SessionFactoryManager manager = SessionFactoryManager.getInstance(name);

        manager.reset();
        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    void testGetServiceRegistry() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getServiceRegistry()).isNotNull();
    }

    @Test
    void testGetSessionFactory() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getSessionFactory()).isNotNull();
    }

    @Test
    void testCreateSession() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        try (Session session = manager.createSession()) {
            assertThat(session).isNotNull();
        }
    }

    @Test
    void testGetTransactionContext() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        HibernateTransactionContext context1 = manager.getTransactionContext();
        HibernateTransactionContext context2 = manager.getTransactionContext();
        assertThat(context1).isSameAs(context2);
    }

    @Test
    void testDropTransactionContext() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getTransactionContext()).isNotNull();
        manager.dropTransactionContext();
        manager.dropTransactionContext();
        assertThat(manager.getTransactionContext()).isNotNull();
    }

    @Test
    void testGetConnectionInfo()
        throws SQLException {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        ConnectionInfo info = manager.getConnectionInfo();
        try (Connection conn = info.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }
}
