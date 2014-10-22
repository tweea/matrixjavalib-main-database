/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import org.junit.Assert;
import org.junit.Test;

/**
 * Hibernate 测试
 * 
 * @author Tweea
 * @version 2005-11-30
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
		Assert.assertSame(tc0, tc01);
		Assert.assertSame(tc1, tc11);
		Assert.assertSame(tc2, tc21);
	}

	@Test
	public void testCreateDrop()
		throws Exception {
		SessionFactoryManager mm = SessionFactoryManager.getInstance();
		Assert.assertNotNull(mm.getTransactionContext());
		mm.dropTransactionContext();
		mm.dropTransactionContext();
		Assert.assertNotNull(mm.getTransactionContext());
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
