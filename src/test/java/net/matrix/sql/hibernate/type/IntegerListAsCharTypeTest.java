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
import net.matrix.sql.hibernate.entity.IntegerListAsCharTestEntity;
import net.matrix.sql.hibernate.entity.IntegerListAsCharTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerListAsCharTypeTest {
    static SessionFactory sessionFactory;

    Transaction transaction;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = SessionFactoryManager.getInstance().getSessionFactory();
    }

    @BeforeEach
    void beforeEach() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testSave() {
        Session session = sessionFactory.getCurrentSession();

        IntegerListAsCharTypeEntity typeEntity = new IntegerListAsCharTypeEntity();
        typeEntity.setValue(Lists.newArrayList(1, 2, 3));
        session.persist(typeEntity);
        session.flush();

        IntegerListAsCharTestEntity testEntity = session.get(IntegerListAsCharTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo("1#2#3");
    }

    @Test
    void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        IntegerListAsCharTestEntity testEntity = new IntegerListAsCharTestEntity();
        testEntity.setValue("1#2#3");
        session.persist(testEntity);
        session.flush();

        IntegerListAsCharTypeEntity typeEntity = session.get(IntegerListAsCharTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).containsExactly(1, 2, 3);
    }
}
