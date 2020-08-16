/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hibernate 测试
 */
public class HibernateTest {
    @Test
    public void testContextManager() {
        SessionFactoryManager mm = SessionFactoryManager.getInstance();
        HibernateTransactionContext tc0 = mm.getTransactionContext();
        HibernateTransactionContext tc1 = mm.getTransactionContext();
        HibernateTransactionContext tc2 = mm.getTransactionContext();
        HibernateTransactionContext tc01 = mm.getTransactionContext();
        HibernateTransactionContext tc11 = mm.getTransactionContext();
        HibernateTransactionContext tc21 = mm.getTransactionContext();
        assertThat(tc01).isSameAs(tc0);
        assertThat(tc11).isSameAs(tc1);
        assertThat(tc21).isSameAs(tc2);
    }

    @Test
    public void testCreateDrop()
        throws SQLException {
        SessionFactoryManager mm = SessionFactoryManager.getInstance();
        assertThat(mm.getTransactionContext()).isNotNull();
        mm.dropTransactionContext();
        mm.dropTransactionContext();
        assertThat(mm.getTransactionContext()).isNotNull();
    }

    @Test
    public void testTransactionContext()
        throws SQLException {
        SessionFactoryManager mm = SessionFactoryManager.getInstance();
        HibernateTransactionContext tc = mm.getTransactionContext();
        tc.begin();
        tc.commit();
        tc.release();
        assertThat(tc.getSession().isJoinedToTransaction()).isFalse();
    }
}
