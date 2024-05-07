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

public class SessionFactoryManagerTest {
    @Test
    public void testGetInstance() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        assertThat(manager.getName()).isEqualTo(SessionFactoryManager.DEFAULT_NAME);

        SessionFactoryManager manager2 = SessionFactoryManager.getInstance(SessionFactoryManager.DEFAULT_NAME);
        assertThat(manager2).isSameAs(manager);
    }

    @Test
    public void testGetInstance_name() {
        String name = "getInstance";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager manager = SessionFactoryManager.getInstance(name);
        assertThat(manager.getName()).isEqualTo(name);
    }

    @Test
    public void testIsNamed() {
        String name = "isNamed";
        SessionFactoryManager.nameInstance(name);

        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    public void testResetAll() {
        String name = "resetAll";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager.resetAll();
        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    public void testClearAll() {
        String name = "clearAll";
        SessionFactoryManager.nameInstance(name);

        SessionFactoryManager.clearAll();
        assertThat(SessionFactoryManager.isNamed(name)).isFalse();
    }

    @Test
    public void testReset() {
        String name = "reset";
        SessionFactoryManager.nameInstance(name);
        SessionFactoryManager manager = SessionFactoryManager.getInstance(name);

        manager.reset();
        assertThat(SessionFactoryManager.isNamed(name)).isTrue();
    }

    @Test
    public void testGetServiceRegistry() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getServiceRegistry()).isNotNull();
    }

    @Test
    public void testGetSessionFactory() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getSessionFactory()).isNotNull();
    }

    @Test
    public void testCreateSession() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        try (Session session = manager.createSession()) {
            assertThat(session).isNotNull();
        }
    }

    @Test
    public void testGetTransactionContext() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        HibernateTransactionContext context1 = manager.getTransactionContext();
        HibernateTransactionContext context2 = manager.getTransactionContext();
        assertThat(context1).isSameAs(context2);
    }

    @Test
    public void testDropTransactionContext() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        assertThat(manager.getTransactionContext()).isNotNull();
        manager.dropTransactionContext();
        manager.dropTransactionContext();
        assertThat(manager.getTransactionContext()).isNotNull();
    }

    @Test
    public void testGetConnectionInfo()
        throws SQLException {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        ConnectionInfo info = manager.getConnectionInfo();
        try (Connection conn = info.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }
}
