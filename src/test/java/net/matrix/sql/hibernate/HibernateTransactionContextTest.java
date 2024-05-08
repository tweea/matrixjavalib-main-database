/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import org.assertj.core.util.introspection.FieldSupport;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HibernateTransactionContextTest {
    private FieldSupport fieldSupport = FieldSupport.extraction();

    @Test
    public void testNew() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();

        HibernateTransactionContext context = new HibernateTransactionContext(manager);
        assertThat(fieldSupport.fieldValue("sessionFactoryManager", SessionFactoryManager.class, context)).isSameAs(manager);
    }

    @Test
    public void testGetSession() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        HibernateTransactionContext context = new HibernateTransactionContext(manager);

        assertThat(context.getSession()).isNotNull();
    }

    @Test
    public void testBegin() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        HibernateTransactionContext context = new HibernateTransactionContext(manager);

        context.begin();
        assertThat(fieldSupport.fieldValue("transaction", Transaction.class, context)).isNotNull();
    }

    @Test
    public void testCommit() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        HibernateTransactionContext context = new HibernateTransactionContext(manager);
        context.begin();

        context.commit();
        assertThat(fieldSupport.fieldValue("transaction", Transaction.class, context)).isNull();
    }

    @Test
    public void testRollback() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        HibernateTransactionContext context = new HibernateTransactionContext(manager);
        context.begin();

        context.rollback();
        assertThat(fieldSupport.fieldValue("transaction", Transaction.class, context)).isNull();
    }

    @Test
    public void testRelease() {
        SessionFactoryManager manager = SessionFactoryManager.getInstance();
        HibernateTransactionContext context = new HibernateTransactionContext(manager);
        context.begin();

        context.release();
        assertThat(fieldSupport.fieldValue("transaction", Transaction.class, context)).isNull();
    }
}
