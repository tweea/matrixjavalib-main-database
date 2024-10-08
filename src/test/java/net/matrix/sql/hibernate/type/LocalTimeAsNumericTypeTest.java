/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.matrix.sql.hibernate.SessionFactoryManager;
import net.matrix.sql.hibernate.entity.LocalTimeAsNumericTestEntity;
import net.matrix.sql.hibernate.entity.LocalTimeAsNumericTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

class LocalTimeAsNumericTypeTest {
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

        LocalTimeAsNumericTypeEntity typeEntity = new LocalTimeAsNumericTypeEntity();
        typeEntity.setValue(LocalTime.of(13, 25, 47));
        session.persist(typeEntity);
        session.flush();

        LocalTimeAsNumericTestEntity testEntity = session.get(LocalTimeAsNumericTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo(13_25_47L);
    }

    @Test
    void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        LocalTimeAsNumericTestEntity testEntity = new LocalTimeAsNumericTestEntity();
        testEntity.setValue(13_25_47L);
        session.persist(testEntity);
        session.flush();

        LocalTimeAsNumericTypeEntity typeEntity = session.get(LocalTimeAsNumericTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).isEqualTo(LocalTime.of(13, 25, 47));
    }
}
