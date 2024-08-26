/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.matrix.sql.hibernate.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateDAOTest {
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
    void testNew() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory) {
        };
        assertThat(dao.getEntityClass()).isSameAs(User.class);
        assertThat(dao.getSessionFactory()).isSameAs(sessionFactory);
        assertThat(dao.isAutoFlush()).isFalse();
    }

    @Test
    void testNew_class() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        assertThat(dao.getEntityClass()).isSameAs(User.class);
        assertThat(dao.getSessionFactory()).isSameAs(sessionFactory);
        assertThat(dao.isAutoFlush()).isFalse();
    }

    @Test
    void testSave() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();

        assertThat(user.getId()).isNull();
        dao.save(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void testSaveAll() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user1 = new User();
        User user2 = new User();

        assertThat(user1.getId()).isNull();
        assertThat(user2.getId()).isNull();
        dao.saveAll(List.of(user1, user2));
        assertThat(user1.getId()).isNotNull();
        assertThat(user2.getId()).isNotNull();
    }

    @Test
    void testFindById() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        user = dao.findById(user.getId()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("test");
    }

    @Test
    void testExistsById() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        assertThat(dao.existsById(user.getId())).isTrue();
    }

    @Test
    void testFindAll() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        List<User> users = dao.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("test");
    }

    @Test
    void testFindAllById() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        List<User> users = dao.findAllById(List.of(user.getId()));
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("test");
    }

    @Test
    void testCount() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        assertThat(dao.count()).isEqualTo(1L);
    }

    @Test
    void testDeleteById() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        dao.deleteById(user.getId());
        assertThat(dao.count()).isZero();
    }

    @Test
    void testDelete() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        dao.delete(user);
        assertThat(dao.count()).isZero();
    }

    @Test
    void testDeleteAllById() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        dao.deleteAllById(List.of(user.getId()));
        assertThat(dao.count()).isZero();
    }

    @Test
    void testDeleteAll_iterable() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        dao.deleteAll(List.of(user));
        assertThat(dao.count()).isZero();
    }

    @Test
    void testDeleteAll() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user = new User();
        user.setName("test");
        dao.save(user);

        dao.deleteAll();
        assertThat(dao.count()).isZero();
    }

    @Test
    void testFindAll_sort() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user1 = new User();
        user1.setName("test1");
        User user2 = new User();
        user2.setName("test2");
        dao.saveAll(List.of(user1, user2));

        List<User> users = dao.findAll(Sort.by("name").descending());
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("test2");
        assertThat(users.get(1).getName()).isEqualTo("test1");
    }

    @Test
    void testFindAll_pageable() {
        HibernateDAO<User, String> dao = new HibernateDAO<>(sessionFactory, User.class);
        User user1 = new User();
        user1.setName("test1");
        User user2 = new User();
        user2.setName("test2");
        dao.saveAll(List.of(user1, user2));

        List<User> users = dao.findAll(PageRequest.of(0, 1, Sort.by("name").descending())).getContent();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("test2");
    }
}
