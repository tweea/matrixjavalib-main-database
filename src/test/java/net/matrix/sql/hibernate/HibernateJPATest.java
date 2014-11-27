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

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDate;
import org.junit.Test;

import net.matrix.sql.hibernate.entity.UserInfo;

/**
 * Hibernate 测试
 */
public class HibernateJPATest {
	@Test
	public void testContextManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		Assertions.assertThat(emf).isNotNull();
		EntityManager em = emf.createEntityManager();
		Assertions.assertThat(em).isNotNull();
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
		Assertions.assertThat(query).isNotNull();
		List<UserInfo> result = query.getResultList();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isEmpty();
		user = result.get(0);
		Assertions.assertThat(user.getYhm()).isEqualTo("abc");
		Assertions.assertThat(user.getMm()).isEqualTo("abc");
		Assertions.assertThat(user.getCsrq()).isEqualTo(new LocalDate(2011, 1, 18));
	}
}
