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
import org.junit.jupiter.api.Test;

import net.matrix.sql.hibernate.entity.UserInfo;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hibernate 测试
 */
public class HibernateJPATest {
    @Test
    public void testContextManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        assertThat(emf).isNotNull();
        EntityManager em = emf.createEntityManager();
        assertThat(em).isNotNull();
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
        assertThat(query).isNotNull();
        List<UserInfo> result = query.getResultList();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        user = result.get(0);
        assertThat(user.getYhm()).isEqualTo("abc");
        assertThat(user.getMm()).isEqualTo("abc");
        assertThat(user.getCsrq()).isEqualTo(new LocalDate(2011, 1, 18));
    }
}
