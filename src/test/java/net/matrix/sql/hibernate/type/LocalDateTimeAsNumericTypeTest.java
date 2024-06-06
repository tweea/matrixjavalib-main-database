/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.matrix.sql.hibernate.SessionFactoryManager;
import net.matrix.sql.hibernate.entity.LocalDateTimeAsNumericTestEntity;
import net.matrix.sql.hibernate.entity.LocalDateTimeAsNumericTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeAsNumericTypeTest {
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

        LocalDateTimeAsNumericTypeEntity typeEntity = new LocalDateTimeAsNumericTypeEntity();
        typeEntity.setValue(LocalDateTime.of(2053, 1, 24, 13, 25, 47));
        session.save(typeEntity);
        session.flush();

        LocalDateTimeAsNumericTestEntity testEntity = session.get(LocalDateTimeAsNumericTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo(2053_01_24_13_25_47L);
    }

    @Test
    void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        LocalDateTimeAsNumericTestEntity testEntity = new LocalDateTimeAsNumericTestEntity();
        testEntity.setValue(2053_01_24_13_25_47L);
        session.save(testEntity);
        session.flush();

        LocalDateTimeAsNumericTypeEntity typeEntity = session.get(LocalDateTimeAsNumericTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).isEqualTo(LocalDateTime.of(2053, 1, 24, 13, 25, 47));
    }
}
