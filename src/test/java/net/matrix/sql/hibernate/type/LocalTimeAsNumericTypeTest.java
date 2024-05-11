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

public class LocalTimeAsNumericTypeTest {
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

        LocalTimeAsNumericTypeEntity typeEntity = new LocalTimeAsNumericTypeEntity();
        typeEntity.setValue(LocalTime.of(13, 25, 47));
        session.save(typeEntity);
        session.flush();

        LocalTimeAsNumericTestEntity testEntity = session.get(LocalTimeAsNumericTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo(13_25_47L);
    }

    @Test
    public void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        LocalTimeAsNumericTestEntity testEntity = new LocalTimeAsNumericTestEntity();
        testEntity.setValue(13_25_47L);
        session.save(testEntity);
        session.flush();

        LocalTimeAsNumericTypeEntity typeEntity = session.get(LocalTimeAsNumericTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).isEqualTo(LocalTime.of(13, 25, 47));
    }
}
