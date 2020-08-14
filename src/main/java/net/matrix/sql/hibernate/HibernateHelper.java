/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 实用类。
 */
public final class HibernateHelper {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(HibernateHelper.class);

    private static final MapListHandler SQL_MAPLIST_HANDLER = new MapListHandler();

    private static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    /**
     * 阻止实例化。
     */
    private HibernateHelper() {
    }

    /**
     * 获得 Hibernate 数据库连接管理对象。
     */
    private static HibernateTransactionContext getTransactionContext() {
        return SessionFactoryManager.getInstance().getTransactionContext();
    }

    /**
     * 获得 Hibernate 数据库连接管理对象。
     */
    private static HibernateTransactionContext getTransactionContext(final String sessionFactoryName) {
        return SessionFactoryManager.getInstance(sessionFactoryName).getTransactionContext();
    }

    private static Session getSession(final HibernateTransactionContext context)
        throws SQLException {
        return context.getSession();
    }

    public static void beginTransaction()
        throws SQLException {
        getTransactionContext().begin();
    }

    public static void beginTransaction(final String sessionFactoryName)
        throws SQLException {
        getTransactionContext(sessionFactoryName).begin();
    }

    public static void commitTransaction()
        throws SQLException {
        getTransactionContext().commit();
    }

    public static void commitTransaction(final String sessionFactoryName)
        throws SQLException {
        getTransactionContext(sessionFactoryName).commit();
    }

    public static void rollbackTransaction()
        throws SQLException {
        getTransactionContext().rollback();
    }

    public static void rollbackTransaction(final String sessionFactoryName)
        throws SQLException {
        getTransactionContext(sessionFactoryName).rollback();
    }

    public static void releaseTransaction() {
        getTransactionContext().release();
    }

    public static void releaseTransaction(final String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).release();
    }

    public static void closeSession() {
        getTransactionContext().release();
    }

    public static void closeSession(final String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).release();
    }

    /**
     * 向数据库中存储一个对象
     */
    public static <T> T merge(final Session session, final T object)
        throws SQLException {
        try {
            return (T) session.merge(object);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 向数据库中存储一个对象
     */
    public static <T> T merge(final HibernateTransactionContext context, final T object)
        throws SQLException {
        return merge(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象
     */
    public static <T> T merge(final T object)
        throws SQLException {
        return merge(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象
     */
    public static <T> T merge(final String sessionFactoryName, final T object)
        throws SQLException {
        return merge(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储一个对象
     */
    public static Serializable create(final Session session, final Object object)
        throws SQLException {
        try {
            return session.save(object);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 向数据库中存储一个对象
     */
    public static Serializable create(final HibernateTransactionContext context, final Object object)
        throws SQLException {
        return create(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象
     */
    public static Serializable create(final Object object)
        throws SQLException {
        return create(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象
     */
    public static Serializable create(final String sessionFactoryName, final Object object)
        throws SQLException {
        return create(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中更新一个对象
     */
    public static void update(final Session session, final Object object)
        throws SQLException {
        try {
            session.update(object);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 向数据库中更新一个对象
     */
    public static void update(final HibernateTransactionContext context, final Object object)
        throws SQLException {
        update(getSession(context), object);
    }

    /**
     * 向数据库中更新一个对象
     */
    public static void update(final Object object)
        throws SQLException {
        update(getTransactionContext(), object);
    }

    /**
     * 向数据库中更新一个对象
     */
    public static void update(final String sessionFactoryName, final Object object)
        throws SQLException {
        update(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储或更新一个对象
     */
    public static void createOrUpdate(final Session session, final Object object)
        throws SQLException {
        try {
            session.saveOrUpdate(object);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 向数据库中存储或更新一个对象
     */
    public static void createOrUpdate(final HibernateTransactionContext context, final Object object)
        throws SQLException {
        createOrUpdate(getSession(context), object);
    }

    /**
     * 向数据库中存储或更新一个对象
     */
    public static void createOrUpdate(final Object object)
        throws SQLException {
        createOrUpdate(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储或更新一个对象
     */
    public static void createOrUpdate(final String sessionFactoryName, final Object object)
        throws SQLException {
        createOrUpdate(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final Session session, final Object object)
        throws SQLException {
        try {
            Object oldObject = session.merge(object);
            session.delete(oldObject);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final HibernateTransactionContext context, final Object object)
        throws SQLException {
        delete(getSession(context), object);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final Object object)
        throws SQLException {
        delete(getTransactionContext(), object);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final String sessionFactoryName, final Object object)
        throws SQLException {
        delete(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final Session session, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        try {
            Object obj = session.load(objectClass, primaryKey);
            session.delete(obj);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final HibernateTransactionContext context, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        delete(getSession(context), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        delete(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象
     */
    public static void delete(final String sessionFactoryName, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        delete(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static <T> T get(final Session session, final Class<T> objectClass, final Serializable primaryKey)
        throws SQLException {
        try {
            return (T) session.get(objectClass, primaryKey);
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static <T> T get(final HibernateTransactionContext context, final Class<T> objectClass, final Serializable primaryKey)
        throws SQLException {
        return get(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static <T> T get(final Class<T> objectClass, final Serializable primaryKey)
        throws SQLException {
        return get(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static <T> T get(final String sessionFactoryName, final Class<T> objectClass, final Serializable primaryKey)
        throws SQLException {
        return get(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static Map<String, Object> getAsMap(final Session session, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        try {
            return (Map) session.get(objectClass, primaryKey);
        } catch (HibernateException e) {
            throw new SQLException(e);
        } catch (ClassCastException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static Map<String, Object> getAsMap(final HibernateTransactionContext context, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        return getAsMap(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static Map<String, Object> getAsMap(final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        return getAsMap(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
     */
    public static Map<String, Object> getAsMap(final String sessionFactoryName, final Class objectClass, final Serializable primaryKey)
        throws SQLException {
        return getAsMap(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    private static void setQueryParameter(final Query query, final Object... parameters) {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(HQLs.getParameterName(i), parameters[i]);
        }
    }

    private static void setQueryParameter(final Query query, final Iterable parameters) {
        int i = 0;
        for (Object param : parameters) {
            query.setParameter(HQLs.getParameterName(i), param);
            i++;
        }
    }

    private static void setQueryParameter(final Query query, final Map<String, ?> parameters) {
        for (Map.Entry<String, ?> paramEntry : parameters.entrySet()) {
            query.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final Session session, final String queryString, final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.executeUpdate();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final HibernateTransactionContext context, final String queryString, final Object... params)
        throws SQLException {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String queryString, final Object... params)
        throws SQLException {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String sessionFactoryName, final String queryString, final Object... params)
        throws SQLException {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final Session session, final String queryString, final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.executeUpdate();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final HibernateTransactionContext context, final String queryString, final Iterable params)
        throws SQLException {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String queryString, final Iterable params)
        throws SQLException {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String sessionFactoryName, final String queryString, final Iterable params)
        throws SQLException {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final Session session, final String queryString, final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.executeUpdate();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final HibernateTransactionContext context, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String queryString, final Map<String, ?> params)
        throws SQLException {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句
     */
    public static int execute(final String sessionFactoryName, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final Session session, final String queryString, final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final HibernateTransactionContext context, final String queryString, final Object... params)
        throws SQLException {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String queryString, final Object... params)
        throws SQLException {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String sessionFactoryName, final String queryString, final Object... params)
        throws SQLException {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final Session session, final String queryString, final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final HibernateTransactionContext context, final String queryString, final Iterable params)
        throws SQLException {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String queryString, final Iterable params)
        throws SQLException {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String sessionFactoryName, final String queryString, final Iterable params)
        throws SQLException {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final Session session, final String queryString, final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final HibernateTransactionContext context, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List queryAll(final String sessionFactoryName, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final Session session, final String queryString, final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final HibernateTransactionContext context, final String queryString, final Object... params)
        throws SQLException {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String queryString, final Object... params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String sessionFactoryName, final String queryString, final Object... params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final Session session, final String queryString, final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final HibernateTransactionContext context, final String queryString, final Iterable params)
        throws SQLException {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String queryString, final Iterable params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String sessionFactoryName, final String queryString, final Iterable params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final Session session, final String queryString, final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final HibernateTransactionContext context, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表
     */
    public static List<Map<String, Object>> queryAllAsMap(final String sessionFactoryName, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final Session session, final String queryString, final int startNum, final int maxResults, final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final HibernateTransactionContext context, final String queryString, final int startNum, final int maxResults,
        final Object... params)
        throws SQLException {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String queryString, final int startNum, final int maxResults, final Object... params)
        throws SQLException {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults, final Object... params)
        throws SQLException {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final Session session, final String queryString, final int startNum, final int maxResults, final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final HibernateTransactionContext context, final String queryString, final int startNum, final int maxResults,
        final Iterable params)
        throws SQLException {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String queryString, final int startNum, final int maxResults, final Iterable params)
        throws SQLException {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults, final Iterable params)
        throws SQLException {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final Session session, final String queryString, final int startNum, final int maxResults, final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final HibernateTransactionContext context, final String queryString, final int startNum, final int maxResults,
        final Map<String, ?> params)
        throws SQLException {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String queryString, final int startNum, final int maxResults, final Map<String, ?> params)
        throws SQLException {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List queryPage(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults,
        final Map<String, ?> params)
        throws SQLException {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final Session session, final String queryString, final int startNum, final int maxResults,
        final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final HibernateTransactionContext context, final String queryString, final int startNum,
        final int maxResults, final Object... params)
        throws SQLException {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String queryString, final int startNum, final int maxResults, final Object... params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults,
        final Object... params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final Session session, final String queryString, final int startNum, final int maxResults,
        final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final HibernateTransactionContext context, final String queryString, final int startNum,
        final int maxResults, final Iterable params)
        throws SQLException {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String queryString, final int startNum, final int maxResults, final Iterable params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults,
        final Iterable params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final Session session, final String queryString, final int startNum, final int maxResults,
        final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return new ArrayList();
        } catch (HibernateException e) {
            throw new SQLException(e);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final HibernateTransactionContext context, final String queryString, final int startNum,
        final int maxResults, final Map<String, ?> params)
        throws SQLException {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String queryString, final int startNum, final int maxResults, final Map<String, ?> params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(final String sessionFactoryName, final String queryString, final int startNum, final int maxResults,
        final Map<String, ?> params)
        throws SQLException {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final Session session, final String queryString, final Object... params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            Object r = query.uniqueResult();
            if (r == null) {
                return 0;
            }
            return ((Number) r).longValue();
        } catch (ObjectNotFoundException oe) {
            LOG.trace("", oe);
            return 0;
        } catch (HibernateException he) {
            throw new SQLException(he);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final HibernateTransactionContext context, final String queryString, final Object... params)
        throws SQLException {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String queryString, final Object... params)
        throws SQLException {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String sessionFactoryName, final String queryString, final Object... params)
        throws SQLException {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final Session session, final String queryString, final Iterable params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            Object r = query.uniqueResult();
            if (r == null) {
                return 0;
            }
            return ((Number) r).longValue();
        } catch (HibernateException he) {
            throw new SQLException(he);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final HibernateTransactionContext context, final String queryString, final Iterable params)
        throws SQLException {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String queryString, final Iterable params)
        throws SQLException {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String sessionFactoryName, final String queryString, final Iterable params)
        throws SQLException {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final Session session, final String queryString, final Map<String, ?> params)
        throws SQLException {
        try {
            Query query = session.createQuery(queryString);
            setQueryParameter(query, params);
            Object r = query.uniqueResult();
            if (r == null) {
                return 0;
            }
            return ((Number) r).longValue();
        } catch (HibernateException he) {
            throw new SQLException(he);
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final HibernateTransactionContext context, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
     */
    public static long queryCount(final String sessionFactoryName, final String queryString, final Map<String, ?> params)
        throws SQLException {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    public static <T> T doReturningWork(final Session session, final ReturningWork<T> work)
        throws SQLException {
        try {
            return session.doReturningWork(work);
        } catch (HibernateException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                throw new SQLException(e);
            } else if (cause instanceof SQLException) {
                throw e;
            } else {
                throw new SQLException(cause);
            }
        }
    }

    public static Integer updateSQL(final Session session, final String sql, final Object... params)
        throws SQLException {
        return doReturningWork(session, new ReturningWork<Integer>() {
            @Override
            public Integer execute(Connection connection)
                throws SQLException {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    if (params != null) {
                        for (int i = 0; i < params.length; i++) {
                            stmt.setObject(i + 1, params[i]);
                        }
                    }
                    return stmt.executeUpdate();
                }
            }
        });
    }

    public static Integer updateSQL(final HibernateTransactionContext context, final String sql, final Object... params)
        throws SQLException {
        return updateSQL(getSession(context), sql, params);
    }

    public static Integer updateSQL(final String sql, final Object... params)
        throws SQLException {
        return updateSQL(getTransactionContext(), sql, params);
    }

    public static Integer updateSQL(final String sessionFactoryName, final String sql, final Object... params)
        throws SQLException {
        return updateSQL(getTransactionContext(sessionFactoryName), sql, params);
    }

    public static List<Map<String, Object>> querySQLAsMap(final Session session, final String sql)
        throws SQLException {
        return doReturningWork(session, new ReturningWork<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> execute(Connection connection)
                throws SQLException {
                try (Statement stmt = connection.createStatement()) {
                    ResultSet rs = stmt.executeQuery(sql);
                    return SQL_MAPLIST_HANDLER.handle(rs);
                }
            }
        });
    }

    public static List<Map<String, Object>> querySQLAsMap(final HibernateTransactionContext context, final String sql)
        throws SQLException {
        return querySQLAsMap(getSession(context), sql);
    }

    public static List<Map<String, Object>> querySQLAsMap(final String sql)
        throws SQLException {
        return querySQLAsMap(getTransactionContext(), sql);
    }

    public static List<Map<String, Object>> querySQLAsMap(final String sessionFactoryName, final String sql)
        throws SQLException {
        return querySQLAsMap(getTransactionContext(sessionFactoryName), sql);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(final Session session, final String sql, final int startNum, final int numPerPage)
        throws SQLException {
        return doReturningWork(session, new ReturningWork<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> execute(Connection connection)
                throws SQLException {
                List<Map<String, Object>> table = new ArrayList<>();

                try (Statement stmt = connection.createStatement()) {
                    ResultSet rs = stmt.executeQuery(sql);
                    for (int index = 0; index < startNum && rs.next(); index++) {
                    }
                    for (int index = 0; rs.next() && index < numPerPage; index++) {
                        Map<String, Object> row = ROW_PROCESSOR.toMap(rs);
                        table.add(row);
                    }
                }

                return table;
            }
        });
    }

    public static List<Map<String, Object>> querySQLPageAsMap(final HibernateTransactionContext context, final String sql, final int startNum,
        final int numPerPage)
        throws SQLException {
        return querySQLPageAsMap(getSession(context), sql, startNum, numPerPage);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(final String sql, final int startNum, final int numPerPage)
        throws SQLException {
        return querySQLPageAsMap(getTransactionContext(), sql, startNum, numPerPage);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(final String sessionFactoryName, final String sql, final int startNum, final int numPerPage)
        throws SQLException {
        return querySQLPageAsMap(getTransactionContext(sessionFactoryName), sql, startNum, numPerPage);
    }

    public static Long querySQLCount(final Session session, final String sql, final Object... params)
        throws SQLException {
        return doReturningWork(session, new ReturningWork<Long>() {
            @Override
            public Long execute(Connection connection)
                throws SQLException {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    if (params != null) {
                        for (int i = 0; i < params.length; i++) {
                            stmt.setObject(i + 1, params[i]);
                        }
                    }
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        return 0L;
                    }
                    return rs.getLong(1);
                }
            }
        });
    }

    public static Long querySQLCount(final HibernateTransactionContext context, final String sql, final Object... params)
        throws SQLException {
        return querySQLCount(getSession(context), sql, params);
    }

    public static Long querySQLCount(final String sql, final Object... params)
        throws SQLException {
        return querySQLCount(getTransactionContext(), sql, params);
    }

    public static Long querySQLCount(final String sessionFactoryName, final String sql, final Object... params)
        throws SQLException {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, params);
    }

    public static Long[] querySQLCount(final Session session, final String sql, final int countNum, final Object... params)
        throws SQLException {
        return doReturningWork(session, new ReturningWork<Long[]>() {
            @Override
            public Long[] execute(Connection connection)
                throws SQLException {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    if (params != null) {
                        for (int i = 0; i < params.length; i++) {
                            stmt.setObject(i + 1, params[i]);
                        }
                    }
                    ResultSet rs = stmt.executeQuery();
                    Long[] result = new Long[countNum];
                    if (rs.next()) {
                        for (int i = 0; i < countNum; i++) {
                            result[i] = rs.getLong(i + 1);
                        }
                    } else {
                        for (int i = 0; i < countNum; i++) {
                            result[i] = 0L;
                        }
                    }
                    return result;
                }
            }
        });
    }

    public static Long[] querySQLCount(final HibernateTransactionContext context, final String sql, final int countNum, final Object... params)
        throws SQLException {
        return querySQLCount(getSession(context), sql, countNum, params);
    }

    public static Long[] querySQLCount(final String sql, final int countNum, final Object... params)
        throws SQLException {
        return querySQLCount(getTransactionContext(), sql, countNum, params);
    }

    public static Long[] querySQLCount(final String sessionFactoryName, final String sql, final int countNum, final Object... params)
        throws SQLException {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, countNum, params);
    }
}
