/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.matrix.java.lang.ClassMx;

/**
 * 使用 Hibernate 原生 API 的泛型 DAO 基类。
 */
public class HibernateDAO<T, ID extends Serializable> {
    /**
     * 实体类。
     */
    private final Class<T> entityClass;

    /**
     * Hibernate 会话工厂。
     */
    private final SessionFactory sessionFactory;

    /**
     * 是否自动调用 flush()。
     */
    private boolean autoFlush;

    /**
     * 构造器，读取子类的泛型定义获取实体类。
     * 
     * @param sessionFactory
     *     Hibernate 会话工厂。
     */
    public HibernateDAO(SessionFactory sessionFactory) {
        this.entityClass = ClassMx.getParameterizedType(getClass(), 0);
        this.sessionFactory = sessionFactory;
    }

    /**
     * 构造器，使用指定的实体类。
     * 
     * @param sessionFactory
     *     Hibernate 会话工厂。
     * @param entityClass
     *     实体类。
     */
    public HibernateDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取实体类。
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 获取 Hibernate 会话工厂。
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 获取是否自动调用 flush()。
     */
    public boolean isAutoFlush() {
        return autoFlush;
    }

    /**
     * 设置是否自动调用 flush()。
     * 
     * @param autoFlush
     *     是否自动调用 flush()。
     */
    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    /**
     * 获取 Hibernate 当前会话。
     */
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 获取 JPA 查询构建器。
     */
    public CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.getCriteriaBuilder();
    }

    /**
     * 保存实体对象。
     * 
     * @param entity
     *     实体对象。
     */
    public <S extends T> S save(S entity) {
        Session session = getCurrentSession();

        session.saveOrUpdate(entity);
        autoFlush(session);
        return entity;
    }

    /**
     * 保存实体对象集合。
     * 
     * @param entities
     *     实体对象集合。
     */
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Session session = getCurrentSession();

        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            session.saveOrUpdate(entity);
            result.add(entity);
        }
        autoFlush(session);
        return result;
    }

    /**
     * 按 id 获取实体对象。
     * 
     * @param id
     *     id。
     */
    public Optional<T> findById(ID id) {
        Session session = getCurrentSession();

        T entity = session.get(entityClass, id);
        return Optional.ofNullable(entity);
    }

    /**
     * 判断指定 id 是否存在对应实体对象。
     * 
     * @param id
     *     id。
     */
    public boolean existsById(ID id) {
        Session session = getCurrentSession();

        T entity = session.get(entityClass, id);
        return entity != null;
    }

    /**
     * 获取所有实体对象集合。
     */
    public List<T> findAll() {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        Query<T> query = session.createQuery(criteriaQuery);
        return query.list();
    }

    /**
     * 按 id 集合获取实体对象集合。
     * 
     * @param ids
     *     id 集合。
     */
    public List<T> findAllById(Iterable<ID> ids) {
        Session session = getCurrentSession();

        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            T entity = session.get(entityClass, id);
            if (entity == null) {
                continue;
            }

            result.add(entity);
        }
        return result;
    }

    /**
     * 统计所有实体对象数量。
     */
    public long count() {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        Query<Long> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    /**
     * 按 id 删除实体对象。
     * 
     * @param id
     *     id。
     */
    public void deleteById(ID id) {
        Session session = getCurrentSession();

        T entity = session.get(entityClass, id);
        if (entity == null) {
            return;
        }

        session.delete(entity);
        autoFlush(session);
    }

    /**
     * 删除实体对象。
     * 
     * @param entity
     *     实体对象。
     */
    public void delete(T entity) {
        Session session = getCurrentSession();

        if (session.contains(entity)) {
            session.delete(entity);
        } else {
            session.delete(session.merge(entity));
        }
        autoFlush(session);
    }

    /**
     * 按 id 集合删除实体对象集合。
     * 
     * @param ids
     *     id 集合。
     */
    public void deleteAllById(Iterable<? extends ID> ids) {
        Session session = getCurrentSession();

        for (ID id : ids) {
            T entity = session.get(entityClass, id);
            if (entity == null) {
                continue;
            }

            session.delete(entity);
        }
        autoFlush(session);
    }

    /**
     * 删除实体对象集合。
     * 
     * @param entities
     *     实体对象集合。
     */
    public void deleteAll(Iterable<? extends T> entities) {
        Session session = getCurrentSession();

        for (T entity : entities) {
            if (session.contains(entity)) {
                session.delete(entity);
            } else {
                session.delete(session.merge(entity));
            }
        }
        autoFlush(session);
    }

    /**
     * 删除所有实体对象集合。
     */
    public void deleteAll() {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);
        criteriaDelete.from(entityClass);
        Query<T> query = session.createQuery(criteriaDelete);
        query.executeUpdate();
    }

    /**
     * 获取所有实体对象集合，支持排序。
     * 
     * @param sort
     *     排序参数。
     */
    public List<T> findAll(Sort sort) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        applySort(criteriaBuilder, criteriaQuery, root, sort);
        Query<T> query = session.createQuery(criteriaQuery);
        return query.list();
    }

    /**
     * 获取所有实体对象集合，支持分页。
     * 
     * @param pageable
     *     分页参数。
     */
    public Page<T> findAll(Pageable pageable) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        applySort(criteriaBuilder, criteriaQuery, root, pageable.getSort());
        Query<T> query = session.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> result = query.list();

        long total = count();
        return new PageImpl(result, pageable, total);
    }

    /**
     * 根据设置自动调用 flush()。
     */
    private void autoFlush(Session session) {
        if (isAutoFlush()) {
            session.flush();
        }
    }

    /**
     * 设置排序。
     */
    private static void applySort(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root root, Sort sort) {
        if (sort == null) {
            return;
        }

        List<Order> jpaOrders = new ArrayList<>();
        for (Sort.Order order : sort) {
            Expression property = root.get(order.getProperty());
            if (order.isIgnoreCase()) {
                property = criteriaBuilder.lower(property);
            }

            Order jpaOrder;
            if (order.isAscending()) {
                jpaOrder = criteriaBuilder.asc(property);
            } else {
                jpaOrder = criteriaBuilder.desc(property);
            }
            jpaOrders.add(jpaOrder);
        }
        criteriaQuery.orderBy(jpaOrders);
    }
}
