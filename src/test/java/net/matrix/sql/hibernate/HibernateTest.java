/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Hibernate 测试
 */
public class HibernateTest {
	@Test
	public void testContextManager()
		throws Exception {
		SessionFactoryManager mm = SessionFactoryManager.getInstance();
		HibernateTransactionContext tc0 = mm.getTransactionContext();
		HibernateTransactionContext tc1 = mm.getTransactionContext();
		HibernateTransactionContext tc2 = mm.getTransactionContext();
		HibernateTransactionContext tc01 = mm.getTransactionContext();
		HibernateTransactionContext tc11 = mm.getTransactionContext();
		HibernateTransactionContext tc21 = mm.getTransactionContext();
		Assertions.assertThat(tc01).isSameAs(tc0);
		Assertions.assertThat(tc11).isSameAs(tc1);
		Assertions.assertThat(tc21).isSameAs(tc2);
	}

	@Test
	public void testCreateDrop()
		throws Exception {
		SessionFactoryManager mm = SessionFactoryManager.getInstance();
		Assertions.assertThat(mm.getTransactionContext()).isNotNull();
		mm.dropTransactionContext();
		mm.dropTransactionContext();
		Assertions.assertThat(mm.getTransactionContext()).isNotNull();
	}

	@Test
	public void testTransactionContext()
		throws Exception {
		SessionFactoryManager mm = SessionFactoryManager.getInstance();
		HibernateTransactionContext tc = mm.getTransactionContext();
		tc.begin();
		tc.commit();
		tc.release();
	}

	@Test
	public void testSetConfigName()
		throws Exception {
		SessionFactoryManager mm = SessionFactoryManager.getInstance();
		HibernateTransactionContext tc = mm.getTransactionContext();
		tc.begin();
		tc.commit();
		tc.release();
	}
}
