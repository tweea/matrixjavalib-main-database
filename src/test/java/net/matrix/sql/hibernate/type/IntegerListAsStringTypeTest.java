/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import net.matrix.sql.hibernate.SessionFactoryManager;
import net.matrix.sql.hibernate.entity.IntegerListAsStringTestEntity;
import net.matrix.sql.hibernate.entity.IntegerListAsStringTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegerListAsStringTypeTest {
    private static SessionFactory sessionFactory;

    private Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        sessionFactory = SessionFactoryManager.getInstance().getSessionFactory();
    }

    @BeforeEach
    public void beforeEach() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    public void afterEach() {
        transaction.rollback();
    }

    @Test
    public void testSave() {
        Session session = sessionFactory.getCurrentSession();

        IntegerListAsStringTypeEntity typeEntity = new IntegerListAsStringTypeEntity();
        typeEntity.setValue(Lists.newArrayList(1, 2, 3));
        session.save(typeEntity);
        session.flush();

        IntegerListAsStringTestEntity testEntity = session.get(IntegerListAsStringTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo("1#2#3");
    }
}
