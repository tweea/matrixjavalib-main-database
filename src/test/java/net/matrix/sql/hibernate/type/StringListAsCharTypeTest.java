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
import net.matrix.sql.hibernate.entity.StringListAsCharTestEntity;
import net.matrix.sql.hibernate.entity.StringListAsCharTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class StringListAsCharTypeTest {
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

        StringListAsCharTypeEntity typeEntity = new StringListAsCharTypeEntity();
        typeEntity.setValue(Lists.newArrayList("a", "b", "c"));
        session.save(typeEntity);
        session.flush();

        StringListAsCharTestEntity testEntity = session.get(StringListAsCharTestEntity.class, typeEntity.getId());
        assertThat(testEntity.getValue()).isEqualTo("a#b#c");
    }

    @Test
    public void testLoad() {
        Session session = sessionFactory.getCurrentSession();

        StringListAsCharTestEntity testEntity = new StringListAsCharTestEntity();
        testEntity.setValue("a#b#c");
        session.save(testEntity);
        session.flush();

        StringListAsCharTypeEntity typeEntity = session.get(StringListAsCharTypeEntity.class, testEntity.getId());
        assertThat(typeEntity.getValue()).containsExactly("a", "b", "c");
    }
}
