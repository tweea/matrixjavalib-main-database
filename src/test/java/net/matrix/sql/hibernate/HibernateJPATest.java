/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import net.matrix.sql.hibernate.entity.UserInfo;

/**
 * Hibernate 测试
 * 
 * @version 2005-11-30
 */
public class HibernateJPATest {
	@Test
	public void testContextManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		Assert.assertNotNull(emf);
		EntityManager em = emf.createEntityManager();
		Assert.assertNotNull(em);
		// 插入
		EntityTransaction et = em.getTransaction();
		et.begin();
		UserInfo user = new UserInfo();
		user.setYhm("abc");
		user.setMm("abc");
		user.setCsrq(new LocalDate(2011, 1, 18));
		em.persist(user);
		et.commit();
		// 查询
		Query query = em.createNamedQuery("UserInfo.findAll");
		Assert.assertNotNull(query);
		List<UserInfo> result = query.getResultList();
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		user = result.get(0);
		Assert.assertEquals("abc", user.getYhm());
		Assert.assertEquals("abc", user.getMm());
		Assert.assertEquals(new LocalDate(2011, 1, 18), user.getCsrq());
	}
}
