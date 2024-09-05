/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 工具。
 */
@ThreadSafe
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
     * 获取 Hibernate 事务上下文。
     */
    @Nonnull
    private static HibernateTransactionContext getTransactionContext() {
        return SessionFactoryManager.getInstance().getTransactionContext();
    }

    /**
     * 获取 Hibernate 事务上下文。
     */
    @Nonnull
    private static HibernateTransactionContext getTransactionContext(@Nonnull String sessionFactoryName) {
        return SessionFactoryManager.getInstance(sessionFactoryName).getTransactionContext();
    }

    private static Session getSession(HibernateTransactionContext context) {
        return context.getSession();
    }

    public static void beginTransaction() {
        getTransactionContext().begin();
    }

    public static void beginTransaction(@Nonnull String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).begin();
    }

    public static void commitTransaction() {
        getTransactionContext().commit();
    }

    public static void commitTransaction(@Nonnull String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).commit();
    }

    public static void rollbackTransaction() {
        getTransactionContext().rollback();
    }

    public static void rollbackTransaction(@Nonnull String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).rollback();
    }

    public static void releaseTransaction() {
        getTransactionContext().release();
    }

    public static void releaseTransaction(@Nonnull String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).release();
    }

    /**
     * 向数据库中存储一个对象。
     */
    @Nonnull
    public static <T> T merge(@Nonnull Session session, @Nonnull T object) {
        return session.merge(object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    @Nonnull
    public static <T> T merge(@Nonnull HibernateTransactionContext context, @Nonnull T object) {
        return merge(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    @Nonnull
    public static <T> T merge(@Nonnull T object) {
        return merge(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    @Nonnull
    public static <T> T merge(@Nonnull String sessionFactoryName, @Nonnull T object) {
        return merge(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static void create(@Nonnull Session session, @Nonnull Object object) {
        session.persist(object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static void create(@Nonnull HibernateTransactionContext context, @Nonnull Object object) {
        create(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static void create(@Nonnull Object object) {
        create(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static void create(@Nonnull String sessionFactoryName, @Nonnull Object object) {
        create(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(@Nonnull Session session, @Nonnull Object object) {
        session.merge(object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(@Nonnull HibernateTransactionContext context, @Nonnull Object object) {
        update(getSession(context), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(@Nonnull Object object) {
        update(getTransactionContext(), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(@Nonnull String sessionFactoryName, @Nonnull Object object) {
        update(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(@Nonnull Session session, @Nonnull Object object) {
        if (session.contains(object)) {
            session.merge(object);
        } else {
            session.persist(object);
        }
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(@Nonnull HibernateTransactionContext context, @Nonnull Object object) {
        createOrUpdate(getSession(context), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(@Nonnull Object object) {
        createOrUpdate(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(@Nonnull String sessionFactoryName, @Nonnull Object object) {
        createOrUpdate(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull Session session, @Nonnull Object object) {
        Object oldObject = session.merge(object);
        session.remove(oldObject);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull HibernateTransactionContext context, @Nonnull Object object) {
        delete(getSession(context), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull Object object) {
        delete(getTransactionContext(), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull String sessionFactoryName, @Nonnull Object object) {
        delete(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull Session session, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        Object obj = session.getReference(objectClass, primaryKey);
        session.remove(obj);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull HibernateTransactionContext context, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        delete(getSession(context), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        delete(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(@Nonnull String sessionFactoryName, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        delete(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static <T> T get(@Nonnull Session session, @Nonnull Class<T> objectClass, @Nonnull Serializable primaryKey) {
        return session.get(objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static <T> T get(@Nonnull HibernateTransactionContext context, @Nonnull Class<T> objectClass, @Nonnull Serializable primaryKey) {
        return get(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static <T> T get(@Nonnull Class<T> objectClass, @Nonnull Serializable primaryKey) {
        return get(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static <T> T get(@Nonnull String sessionFactoryName, @Nonnull Class<T> objectClass, @Nonnull Serializable primaryKey) {
        return get(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static Map<String, Object> getAsMap(@Nonnull Session session, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        return (Map) session.get(objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static Map<String, Object> getAsMap(@Nonnull HibernateTransactionContext context, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        return getAsMap(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static Map<String, Object> getAsMap(@Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        return getAsMap(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    @Nullable
    public static Map<String, Object> getAsMap(@Nonnull String sessionFactoryName, @Nonnull Class objectClass, @Nonnull Serializable primaryKey) {
        return getAsMap(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    private static void setQueryParameter(Query query, Object... parameters) {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(HQLmx.getParameterName(i), parameters[i]);
        }
    }

    private static void setQueryParameter(Query query, Iterable parameters) {
        int i = 0;
        for (Object param : parameters) {
            query.setParameter(HQLmx.getParameterName(i), param);
            ++i;
        }
    }

    private static void setQueryParameter(Query query, Map<String, ?> parameters) {
        for (Map.Entry<String, ?> paramEntry : parameters.entrySet()) {
            query.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
    }

    private static void setQueryParameter(MutationQuery query, Object... parameters) {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(HQLmx.getParameterName(i), parameters[i]);
        }
    }

    private static void setQueryParameter(MutationQuery query, Iterable parameters) {
        int i = 0;
        for (Object param : parameters) {
            query.setParameter(HQLmx.getParameterName(i), param);
            ++i;
        }
    }

    private static void setQueryParameter(MutationQuery query, Map<String, ?> parameters) {
        for (Map.Entry<String, ?> paramEntry : parameters.entrySet()) {
            query.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull Session session, @Nonnull String queryString, Object... params) {
        MutationQuery query = session.createMutationQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, Object... params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String queryString, Object... params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String sessionFactoryName, @Nonnull String queryString, Object... params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull Session session, @Nonnull String queryString, @Nonnull Iterable params) {
        MutationQuery query = session.createMutationQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Iterable params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String queryString, @Nonnull Iterable params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Iterable params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull Session session, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        MutationQuery query = session.createMutationQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull Session session, @Nonnull String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, Object... params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String queryString, Object... params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String sessionFactoryName, @Nonnull String queryString, Object... params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull Session session, @Nonnull String queryString, @Nonnull Iterable params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String queryString, @Nonnull Iterable params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull Session session, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List queryAll(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull Session session, @Nonnull String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, Object... params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String queryString, Object... params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, Object... params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull Session session, @Nonnull String queryString, @Nonnull Iterable params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String queryString, @Nonnull Iterable params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull Session session, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString,
        @Nonnull Map<String, ?> params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    @Nonnull
    public static List<Map<String, Object>> queryAllAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults, Object... params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults, @Nonnull Iterable params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Iterable params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String queryString, int startNum, int maxResults, @Nonnull Iterable params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults, @Nonnull Iterable params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults, @Nonnull Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString, Object.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Map<String, ?> params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String queryString, int startNum, int maxResults, @Nonnull Map<String, ?> params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List queryPage(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Map<String, ?> params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults,
        Object... params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum,
        int maxResults, Object... params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String queryString, int startNum, int maxResults, Object... params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults,
        Object... params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Iterable params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum,
        int maxResults, @Nonnull Iterable params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String queryString, int startNum, int maxResults, @Nonnull Iterable params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Iterable params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull Session session, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString, Map.class);
            setQueryParameter(query, params);
            query.setFirstResult(startNum);
            query.setMaxResults(maxResults);
            return query.list();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return new ArrayList();
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, int startNum,
        int maxResults, @Nonnull Map<String, ?> params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String queryString, int startNum, int maxResults, @Nonnull Map<String, ?> params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    @Nonnull
    public static List<Map<String, Object>> queryPageAsMap(@Nonnull String sessionFactoryName, @Nonnull String queryString, int startNum, int maxResults,
        @Nonnull Map<String, ?> params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull Session session, @Nonnull String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString, Long.class);
            setQueryParameter(query, params);
            Object r = query.uniqueResult();
            if (r == null) {
                return 0;
            }
            return ((Number) r).longValue();
        } catch (ObjectNotFoundException e) {
            LOG.trace("", e);
            return 0;
        }
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, Object... params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String queryString, Object... params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String sessionFactoryName, @Nonnull String queryString, Object... params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull Session session, @Nonnull String queryString, @Nonnull Iterable params) {
        Query query = session.createQuery(queryString, Long.class);
        setQueryParameter(query, params);
        Object r = query.uniqueResult();
        if (r == null) {
            return 0;
        }
        return ((Number) r).longValue();
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String queryString, @Nonnull Iterable params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Iterable params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull Session session, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        Query query = session.createQuery(queryString, Long.class);
        setQueryParameter(query, params);
        Object r = query.uniqueResult();
        if (r == null) {
            return 0;
        }
        return ((Number) r).longValue();
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull HibernateTransactionContext context, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整型返回值。
     */
    public static long queryCount(@Nonnull String sessionFactoryName, @Nonnull String queryString, @Nonnull Map<String, ?> params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    public static <T> T doReturningWork(@Nonnull Session session, @Nonnull ReturningWork<T> work) {
        return session.doReturningWork(work);
    }

    public static int updateSQL(@Nonnull Session session, @Nonnull String sql, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; ++i) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                return stmt.executeUpdate();
            }
        });
    }

    public static int updateSQL(@Nonnull HibernateTransactionContext context, @Nonnull String sql, Object... params) {
        return updateSQL(getSession(context), sql, params);
    }

    public static int updateSQL(@Nonnull String sql, Object... params) {
        return updateSQL(getTransactionContext(), sql, params);
    }

    public static int updateSQL(@Nonnull String sessionFactoryName, @Nonnull String sql, Object... params) {
        return updateSQL(getTransactionContext(sessionFactoryName), sql, params);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLAsMap(@Nonnull Session session, @Nonnull String sql) {
        return doReturningWork(session, connection -> {
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return SQL_MAPLIST_HANDLER.handle(rs);
            }
        });
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String sql) {
        return querySQLAsMap(getSession(context), sql);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLAsMap(@Nonnull String sql) {
        return querySQLAsMap(getTransactionContext(), sql);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLAsMap(@Nonnull String sessionFactoryName, @Nonnull String sql) {
        return querySQLAsMap(getTransactionContext(sessionFactoryName), sql);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLPageAsMap(@Nonnull Session session, @Nonnull String sql, int startNum, int numPerPage) {
        return doReturningWork(session, connection -> {
            List<Map<String, Object>> table = new ArrayList<>();

            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                for (int index = 0; index < startNum && rs.next(); ++index) {
                    // 空循环，跳过前面的记录
                }
                for (int index = 0; rs.next() && index < numPerPage; ++index) {
                    Map<String, Object> row = ROW_PROCESSOR.toMap(rs);
                    table.add(row);
                }
            }

            return table;
        });
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLPageAsMap(@Nonnull HibernateTransactionContext context, @Nonnull String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getSession(context), sql, startNum, numPerPage);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLPageAsMap(@Nonnull String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getTransactionContext(), sql, startNum, numPerPage);
    }

    @Nonnull
    public static List<Map<String, Object>> querySQLPageAsMap(@Nonnull String sessionFactoryName, @Nonnull String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getTransactionContext(sessionFactoryName), sql, startNum, numPerPage);
    }

    public static long querySQLCount(@Nonnull Session session, @Nonnull String sql, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; ++i) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return 0L;
                    }
                    return rs.getLong(1);
                }
            }
        });
    }

    public static long querySQLCount(@Nonnull HibernateTransactionContext context, @Nonnull String sql, Object... params) {
        return querySQLCount(getSession(context), sql, params);
    }

    public static long querySQLCount(@Nonnull String sql, Object... params) {
        return querySQLCount(getTransactionContext(), sql, params);
    }

    public static long querySQLCount(@Nonnull String sessionFactoryName, @Nonnull String sql, Object... params) {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, params);
    }

    @Nonnull
    public static long[] querySQLCount(@Nonnull Session session, @Nonnull String sql, int countNum, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; ++i) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    long[] result = new long[countNum];
                    if (rs.next()) {
                        for (int i = 0; i < countNum; ++i) {
                            result[i] = rs.getLong(i + 1);
                        }
                    } else {
                        for (int i = 0; i < countNum; ++i) {
                            result[i] = 0L;
                        }
                    }
                    return result;
                }
            }
        });
    }

    @Nonnull
    public static long[] querySQLCount(@Nonnull HibernateTransactionContext context, @Nonnull String sql, int countNum, Object... params) {
        return querySQLCount(getSession(context), sql, countNum, params);
    }

    @Nonnull
    public static long[] querySQLCount(@Nonnull String sql, int countNum, Object... params) {
        return querySQLCount(getTransactionContext(), sql, countNum, params);
    }

    @Nonnull
    public static long[] querySQLCount(@Nonnull String sessionFactoryName, @Nonnull String sql, int countNum, Object... params) {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, countNum, params);
    }
}
