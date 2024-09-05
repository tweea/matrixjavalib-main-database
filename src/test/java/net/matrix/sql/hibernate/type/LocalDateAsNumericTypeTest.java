/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.matrix.sql.hibernate.SessionFactoryManager;
import net.matrix.sql.hibernate.entity.LocalDateAsNumericTestEntity;
import net.matrix.sql.hibernate.entity.LocalDateAsNumericTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateAsNumericTypeTest {
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

        LocalDateAsNumericTypeEntity typeEntity = new LocalDateAsNumericTypeEntity();
        typeEntity.setValue(LocalDate.of(2053, 1, 24));
        session.persist(typeEntity);
        session.flush();

        LocalDateAsNumericTestEntity testEntity = session.get(LocalDateAsNumericTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo(2053_01_24L);
    }

    @Test
    void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        LocalDateAsNumericTestEntity testEntity = new LocalDateAsNumericTestEntity();
        testEntity.setValue(2053_01_24L);
        session.persist(testEntity);
        session.flush();

        LocalDateAsNumericTypeEntity typeEntity = session.get(LocalDateAsNumericTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).isEqualTo(LocalDate.of(2053, 1, 24));
    }
}
