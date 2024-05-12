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

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 工具。
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
     * 获取 Hibernate 事务上下文。
     */
    private static HibernateTransactionContext getTransactionContext() {
        return SessionFactoryManager.getInstance().getTransactionContext();
    }

    /**
     * 获取 Hibernate 事务上下文。
     */
    private static HibernateTransactionContext getTransactionContext(String sessionFactoryName) {
        return SessionFactoryManager.getInstance(sessionFactoryName).getTransactionContext();
    }

    private static Session getSession(HibernateTransactionContext context) {
        return context.getSession();
    }

    public static void beginTransaction() {
        getTransactionContext().begin();
    }

    public static void beginTransaction(String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).begin();
    }

    public static void commitTransaction() {
        getTransactionContext().commit();
    }

    public static void commitTransaction(String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).commit();
    }

    public static void rollbackTransaction() {
        getTransactionContext().rollback();
    }

    public static void rollbackTransaction(String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).rollback();
    }

    public static void releaseTransaction() {
        getTransactionContext().release();
    }

    public static void releaseTransaction(String sessionFactoryName) {
        getTransactionContext(sessionFactoryName).release();
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static <T> T merge(Session session, T object) {
        return (T) session.merge(object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static <T> T merge(HibernateTransactionContext context, T object) {
        return merge(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static <T> T merge(T object) {
        return merge(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static <T> T merge(String sessionFactoryName, T object) {
        return merge(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static Serializable create(Session session, Object object) {
        return session.save(object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static Serializable create(HibernateTransactionContext context, Object object) {
        return create(getSession(context), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static Serializable create(Object object) {
        return create(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储一个对象。
     */
    public static Serializable create(String sessionFactoryName, Object object) {
        return create(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(Session session, Object object) {
        session.update(object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(HibernateTransactionContext context, Object object) {
        update(getSession(context), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(Object object) {
        update(getTransactionContext(), object);
    }

    /**
     * 向数据库中更新一个对象。
     */
    public static void update(String sessionFactoryName, Object object) {
        update(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(Session session, Object object) {
        session.saveOrUpdate(object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(HibernateTransactionContext context, Object object) {
        createOrUpdate(getSession(context), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(Object object) {
        createOrUpdate(getTransactionContext(), object);
    }

    /**
     * 向数据库中存储或更新一个对象。
     */
    public static void createOrUpdate(String sessionFactoryName, Object object) {
        createOrUpdate(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(Session session, Object object) {
        Object oldObject = session.merge(object);
        session.delete(oldObject);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(HibernateTransactionContext context, Object object) {
        delete(getSession(context), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(Object object) {
        delete(getTransactionContext(), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(String sessionFactoryName, Object object) {
        delete(getTransactionContext(sessionFactoryName), object);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(Session session, Class objectClass, Serializable primaryKey) {
        Object obj = session.load(objectClass, primaryKey);
        session.delete(obj);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(HibernateTransactionContext context, Class objectClass, Serializable primaryKey) {
        delete(getSession(context), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(Class objectClass, Serializable primaryKey) {
        delete(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 从数据库中删除一个对象。
     */
    public static void delete(String sessionFactoryName, Class objectClass, Serializable primaryKey) {
        delete(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static <T> T get(Session session, Class<T> objectClass, Serializable primaryKey) {
        return session.get(objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static <T> T get(HibernateTransactionContext context, Class<T> objectClass, Serializable primaryKey) {
        return get(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static <T> T get(Class<T> objectClass, Serializable primaryKey) {
        return get(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static <T> T get(String sessionFactoryName, Class<T> objectClass, Serializable primaryKey) {
        return get(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static Map<String, Object> getAsMap(Session session, Class objectClass, Serializable primaryKey) {
        return (Map) session.get(objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static Map<String, Object> getAsMap(HibernateTransactionContext context, Class objectClass, Serializable primaryKey) {
        return getAsMap(getSession(context), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static Map<String, Object> getAsMap(Class objectClass, Serializable primaryKey) {
        return getAsMap(getTransactionContext(), objectClass, primaryKey);
    }

    /**
     * 根据类型和主键从数据库中获取一个对象，若没有则返回 null。
     */
    public static Map<String, Object> getAsMap(String sessionFactoryName, Class objectClass, Serializable primaryKey) {
        return getAsMap(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
    }

    private static void setQueryParameter(Query query, Object... parameters) {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(HQLmx.getParameterName(i), parameters[i]);
        }
    }

    private static void setQueryParameter(Query query, Iterable parameters) {
        int i = 0;
        for (Object param : parameters) {
            query.setParameter(HQLmx.getParameterName(i), param);
            i++;
        }
    }

    private static void setQueryParameter(Query query, Map<String, ?> parameters) {
        for (Map.Entry<String, ?> paramEntry : parameters.entrySet()) {
            query.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(Session session, String queryString, Object... params) {
        Query query = session.createQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(HibernateTransactionContext context, String queryString, Object... params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String queryString, Object... params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String sessionFactoryName, String queryString, Object... params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(Session session, String queryString, Iterable params) {
        Query query = session.createQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(HibernateTransactionContext context, String queryString, Iterable params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String queryString, Iterable params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String sessionFactoryName, String queryString, Iterable params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(Session session, String queryString, Map<String, ?> params) {
        Query query = session.createQuery(queryString);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(HibernateTransactionContext context, String queryString, Map<String, ?> params) {
        return execute(getSession(context), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String queryString, Map<String, ?> params) {
        return execute(getTransactionContext(), queryString, params);
    }

    /**
     * 执行 HQL 语句。
     */
    public static int execute(String sessionFactoryName, String queryString, Map<String, ?> params) {
        return execute(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(Session session, String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryAll(HibernateTransactionContext context, String queryString, Object... params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String queryString, Object... params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String sessionFactoryName, String queryString, Object... params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(Session session, String queryString, Iterable params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryAll(HibernateTransactionContext context, String queryString, Iterable params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String queryString, Iterable params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String sessionFactoryName, String queryString, Iterable params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(Session session, String queryString, Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryAll(HibernateTransactionContext context, String queryString, Map<String, ?> params) {
        return queryAll(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String queryString, Map<String, ?> params) {
        return queryAll(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List queryAll(String sessionFactoryName, String queryString, Map<String, ?> params) {
        return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Object... params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String queryString, Object... params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Object... params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Iterable params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Iterable params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String queryString, Iterable params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Iterable params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Map<String, ?> params) {
        return queryAllAsMap(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String queryString, Map<String, ?> params) {
        return queryAllAsMap(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表。
     */
    public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Map<String, ?> params) {
        return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(Session session, String queryString, int startNum, int maxResults, Object... params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Object... params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(Session session, String queryString, int startNum, int maxResults, Iterable params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Iterable params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String queryString, int startNum, int maxResults, Iterable params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Iterable params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(Session session, String queryString, int startNum, int maxResults, Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Map<String, ?> params) {
        return queryPage(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String queryString, int startNum, int maxResults, Map<String, ?> params) {
        return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Map<String, ?> params) {
        return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Object... params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
        Object... params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Object... params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Object... params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Iterable params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
        Iterable params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Iterable params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Iterable params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Map<String, ?> params) {
        try {
            Query query = session.createQuery(queryString);
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
    public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
        Map<String, ?> params) {
        return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Map<String, ?> params) {
        return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取对象列表，限定起始结果和行数。
     */
    public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Map<String, ?> params) {
        return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(Session session, String queryString, Object... params) {
        try {
            Query query = session.createQuery(queryString);
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
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(HibernateTransactionContext context, String queryString, Object... params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String queryString, Object... params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String sessionFactoryName, String queryString, Object... params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(Session session, String queryString, Iterable params) {
        Query query = session.createQuery(queryString);
        setQueryParameter(query, params);
        Object r = query.uniqueResult();
        if (r == null) {
            return 0;
        }
        return ((Number) r).longValue();
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(HibernateTransactionContext context, String queryString, Iterable params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String queryString, Iterable params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String sessionFactoryName, String queryString, Iterable params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(Session session, String queryString, Map<String, ?> params) {
        Query query = session.createQuery(queryString);
        setQueryParameter(query, params);
        Object r = query.uniqueResult();
        if (r == null) {
            return 0;
        }
        return ((Number) r).longValue();
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(HibernateTransactionContext context, String queryString, Map<String, ?> params) {
        return queryCount(getSession(context), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String queryString, Map<String, ?> params) {
        return queryCount(getTransactionContext(), queryString, params);
    }

    /**
     * 根据 HQL 查询字符串和参数从数据库中获取整形返回值。
     */
    public static long queryCount(String sessionFactoryName, String queryString, Map<String, ?> params) {
        return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
    }

    public static <T> T doReturningWork(Session session, ReturningWork<T> work) {
        return session.doReturningWork(work);
    }

    public static Integer updateSQL(Session session, String sql, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                return stmt.executeUpdate();
            }
        });
    }

    public static Integer updateSQL(HibernateTransactionContext context, String sql, Object... params) {
        return updateSQL(getSession(context), sql, params);
    }

    public static Integer updateSQL(String sql, Object... params) {
        return updateSQL(getTransactionContext(), sql, params);
    }

    public static Integer updateSQL(String sessionFactoryName, String sql, Object... params) {
        return updateSQL(getTransactionContext(sessionFactoryName), sql, params);
    }

    public static List<Map<String, Object>> querySQLAsMap(Session session, String sql) {
        return doReturningWork(session, connection -> {
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return SQL_MAPLIST_HANDLER.handle(rs);
            }
        });
    }

    public static List<Map<String, Object>> querySQLAsMap(HibernateTransactionContext context, String sql) {
        return querySQLAsMap(getSession(context), sql);
    }

    public static List<Map<String, Object>> querySQLAsMap(String sql) {
        return querySQLAsMap(getTransactionContext(), sql);
    }

    public static List<Map<String, Object>> querySQLAsMap(String sessionFactoryName, String sql) {
        return querySQLAsMap(getTransactionContext(sessionFactoryName), sql);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(Session session, String sql, int startNum, int numPerPage) {
        return doReturningWork(session, connection -> {
            List<Map<String, Object>> table = new ArrayList<>();

            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                for (int index = 0; index < startNum && rs.next(); index++) {
                    // 空循环，跳过前面的记录
                }
                for (int index = 0; rs.next() && index < numPerPage; index++) {
                    Map<String, Object> row = ROW_PROCESSOR.toMap(rs);
                    table.add(row);
                }
            }

            return table;
        });
    }

    public static List<Map<String, Object>> querySQLPageAsMap(HibernateTransactionContext context, String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getSession(context), sql, startNum, numPerPage);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getTransactionContext(), sql, startNum, numPerPage);
    }

    public static List<Map<String, Object>> querySQLPageAsMap(String sessionFactoryName, String sql, int startNum, int numPerPage) {
        return querySQLPageAsMap(getTransactionContext(sessionFactoryName), sql, startNum, numPerPage);
    }

    public static Long querySQLCount(Session session, String sql, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
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

    public static Long querySQLCount(HibernateTransactionContext context, String sql, Object... params) {
        return querySQLCount(getSession(context), sql, params);
    }

    public static Long querySQLCount(String sql, Object... params) {
        return querySQLCount(getTransactionContext(), sql, params);
    }

    public static Long querySQLCount(String sessionFactoryName, String sql, Object... params) {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, params);
    }

    public static Long[] querySQLCount(Session session, String sql, int countNum, Object... params) {
        return doReturningWork(session, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                try (ResultSet rs = stmt.executeQuery()) {
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

    public static Long[] querySQLCount(HibernateTransactionContext context, String sql, int countNum, Object... params) {
        return querySQLCount(getSession(context), sql, countNum, params);
    }

    public static Long[] querySQLCount(String sql, int countNum, Object... params) {
        return querySQLCount(getTransactionContext(), sql, countNum, params);
    }

    public static Long[] querySQLCount(String sessionFactoryName, String sql, int countNum, Object... params) {
        return querySQLCount(getTransactionContext(sessionFactoryName), sql, countNum, params);
    }
}
