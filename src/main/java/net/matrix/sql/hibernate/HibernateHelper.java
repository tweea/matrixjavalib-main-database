/*
 * $Id: HibernateHelper.java 901 2014-01-22 16:36:15Z tweea@263.net $
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import net.matrix.lang.Objects2;

/**
 * Hibernate 实用类。
 * 
 * @since 2005.06.15
 */
public final class HibernateHelper {
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
	private static HibernateTransactionContext getTransactionContext(String sessionFactoryName) {
		return SessionFactoryManager.getInstance(sessionFactoryName).getTransactionContext();
	}

	private static Session getSession(HibernateTransactionContext context)
		throws SQLException {
		return context.getSession();
	}

	public static void beginTransaction()
		throws SQLException {
		getTransactionContext().begin();
	}

	public static void beginTransaction(String sessionFactoryName)
		throws SQLException {
		getTransactionContext(sessionFactoryName).begin();
	}

	public static void commitTransaction()
		throws SQLException {
		getTransactionContext().commit();
	}

	public static void commitTransaction(String sessionFactoryName)
		throws SQLException {
		getTransactionContext(sessionFactoryName).commit();
	}

	public static void rollbackTransaction()
		throws SQLException {
		getTransactionContext().rollback();
	}

	public static void rollbackTransaction(String sessionFactoryName)
		throws SQLException {
		getTransactionContext(sessionFactoryName).rollback();
	}

	public static void releaseTransaction() {
		getTransactionContext().release();
	}

	public static void releaseTransaction(String sessionFactoryName) {
		getTransactionContext(sessionFactoryName).release();
	}

	public static void closeSession() {
		getTransactionContext().release();
	}

	public static void closeSession(String sessionFactoryName) {
		getTransactionContext(sessionFactoryName).release();
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static <T> T merge(Session session, T object)
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
	public static <T> T merge(HibernateTransactionContext context, T object)
		throws SQLException {
		return merge(getSession(context), object);
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static <T> T merge(T object)
		throws SQLException {
		return merge(getTransactionContext(), object);
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static <T> T merge(String sessionFactoryName, T object)
		throws SQLException {
		return merge(getTransactionContext(sessionFactoryName), object);
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static Serializable create(Session session, Object object)
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
	public static Serializable create(HibernateTransactionContext context, Object object)
		throws SQLException {
		return create(getSession(context), object);
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static Serializable create(Object object)
		throws SQLException {
		return create(getTransactionContext(), object);
	}

	/**
	 * 向数据库中存储一个对象
	 */
	public static Serializable create(String sessionFactoryName, Object object)
		throws SQLException {
		return create(getTransactionContext(sessionFactoryName), object);
	}

	/**
	 * 向数据库中更新一个对象
	 */
	public static void update(Session session, Object object)
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
	public static void update(HibernateTransactionContext context, Object object)
		throws SQLException {
		update(getSession(context), object);
	}

	/**
	 * 向数据库中更新一个对象
	 */
	public static void update(Object object)
		throws SQLException {
		update(getTransactionContext(), object);
	}

	/**
	 * 向数据库中更新一个对象
	 */
	public static void update(String sessionFactoryName, Object object)
		throws SQLException {
		update(getTransactionContext(sessionFactoryName), object);
	}

	/**
	 * 向数据库中存储或更新一个对象
	 */
	public static void createOrUpdate(Session session, Object object)
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
	public static void createOrUpdate(HibernateTransactionContext context, Object object)
		throws SQLException {
		createOrUpdate(getSession(context), object);
	}

	/**
	 * 向数据库中存储或更新一个对象
	 */
	public static void createOrUpdate(Object object)
		throws SQLException {
		createOrUpdate(getTransactionContext(), object);
	}

	/**
	 * 向数据库中存储或更新一个对象
	 */
	public static void createOrUpdate(String sessionFactoryName, Object object)
		throws SQLException {
		createOrUpdate(getTransactionContext(sessionFactoryName), object);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(Session session, Object object)
		throws SQLException {
		try {
			object = session.merge(object);
			session.delete(object);
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(HibernateTransactionContext context, Object object)
		throws SQLException {
		delete(getSession(context), object);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(Object object)
		throws SQLException {
		delete(getTransactionContext(), object);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(String sessionFactoryName, Object object)
		throws SQLException {
		delete(getTransactionContext(sessionFactoryName), object);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(Session session, Class objectClass, Serializable primaryKey)
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
	public static void delete(HibernateTransactionContext context, Class objectClass, Serializable primaryKey)
		throws SQLException {
		delete(getSession(context), objectClass, primaryKey);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(Class objectClass, Serializable primaryKey)
		throws SQLException {
		delete(getTransactionContext(), objectClass, primaryKey);
	}

	/**
	 * 从数据库中删除一个对象
	 */
	public static void delete(String sessionFactoryName, Class objectClass, Serializable primaryKey)
		throws SQLException {
		delete(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static <T> T get(Session session, Class<T> objectClass, Serializable primaryKey)
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
	public static <T> T get(HibernateTransactionContext context, Class<T> objectClass, Serializable primaryKey)
		throws SQLException {
		return get(getSession(context), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static <T> T get(Class<T> objectClass, Serializable primaryKey)
		throws SQLException {
		return get(getTransactionContext(), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static <T> T get(String sessionFactoryName, Class<T> objectClass, Serializable primaryKey)
		throws SQLException {
		return get(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static Map<String, Object> getAsMap(Session session, Class objectClass, Serializable primaryKey)
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
	public static Map<String, Object> getAsMap(HibernateTransactionContext context, Class objectClass, Serializable primaryKey)
		throws SQLException {
		return getAsMap(getSession(context), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static Map<String, Object> getAsMap(Class objectClass, Serializable primaryKey)
		throws SQLException {
		return getAsMap(getTransactionContext(), objectClass, primaryKey);
	}

	/**
	 * 根据类型和主键从数据库中获得一个对象，若没有则返回 null
	 */
	public static Map<String, Object> getAsMap(String sessionFactoryName, Class objectClass, Serializable primaryKey)
		throws SQLException {
		return getAsMap(getTransactionContext(sessionFactoryName), objectClass, primaryKey);
	}

	private static void setQueryParameter(Query query, Object... parameters) {
		if (parameters == null) {
			return;
		}
		for (int i = 0; i < parameters.length; i++) {
			query.setParameter(HQLs.getParameterName(i), parameters[i]);
		}
	}

	private static void setQueryParameter(Query query, Iterable parameters) {
		int i = 0;
		for (Object param : parameters) {
			query.setParameter(HQLs.getParameterName(i), param);
			i++;
		}
	}

	private static void setQueryParameter(Query query, Map<String, ?> parameters) {
		for (Map.Entry<String, ?> paramEntry : parameters.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(Session session, String queryString, Object... params)
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
	public static int execute(HibernateTransactionContext context, String queryString, Object... params)
		throws SQLException {
		return execute(getSession(context), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String queryString, Object... params)
		throws SQLException {
		return execute(getTransactionContext(), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String sessionFactoryName, String queryString, Object... params)
		throws SQLException {
		return execute(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(Session session, String queryString, Iterable params)
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
	public static int execute(HibernateTransactionContext context, String queryString, Iterable params)
		throws SQLException {
		return execute(getSession(context), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String queryString, Iterable params)
		throws SQLException {
		return execute(getTransactionContext(), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String sessionFactoryName, String queryString, Iterable params)
		throws SQLException {
		return execute(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(Session session, String queryString, Map<String, ?> params)
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
	public static int execute(HibernateTransactionContext context, String queryString, Map<String, ?> params)
		throws SQLException {
		return execute(getSession(context), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String queryString, Map<String, ?> params)
		throws SQLException {
		return execute(getTransactionContext(), queryString, params);
	}

	/**
	 * 执行 HQL 语句
	 */
	public static int execute(String sessionFactoryName, String queryString, Map<String, ?> params)
		throws SQLException {
		return execute(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(Session session, String queryString, Object... params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(HibernateTransactionContext context, String queryString, Object... params)
		throws SQLException {
		return queryAll(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String queryString, Object... params)
		throws SQLException {
		return queryAll(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String sessionFactoryName, String queryString, Object... params)
		throws SQLException {
		return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(Session session, String queryString, Iterable params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(HibernateTransactionContext context, String queryString, Iterable params)
		throws SQLException {
		return queryAll(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String queryString, Iterable params)
		throws SQLException {
		return queryAll(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String sessionFactoryName, String queryString, Iterable params)
		throws SQLException {
		return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(Session session, String queryString, Map<String, ?> params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(HibernateTransactionContext context, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAll(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAll(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List queryAll(String sessionFactoryName, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAll(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Object... params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Object... params)
		throws SQLException {
		return queryAllAsMap(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String queryString, Object... params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Object... params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Iterable params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Iterable params)
		throws SQLException {
		return queryAllAsMap(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String queryString, Iterable params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Iterable params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(Session session, String queryString, Map<String, ?> params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(HibernateTransactionContext context, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAllAsMap(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表
	 */
	public static List<Map<String, Object>> queryAllAsMap(String sessionFactoryName, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryAllAsMap(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(Session session, String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		return queryPage(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(Session session, String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		return queryPage(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(Session session, String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(HibernateTransactionContext context, String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		return queryPage(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		return queryPage(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List queryPage(String sessionFactoryName, String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		return queryPage(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
		Object... params)
		throws SQLException {
		return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Object... params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
		Iterable params)
		throws SQLException {
		return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Iterable params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(Session session, String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		try {
			Query query = session.createQuery(queryString);
			setQueryParameter(query, params);
			query.setFirstResult(startNum);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (ObjectNotFoundException oe) {
			return new ArrayList();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(HibernateTransactionContext context, String queryString, int startNum, int maxResults,
		Map<String, ?> params)
		throws SQLException {
		return queryPageAsMap(getSession(context), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得对象列表，限定起始结果和行数。
	 */
	public static List<Map<String, Object>> queryPageAsMap(String sessionFactoryName, String queryString, int startNum, int maxResults, Map<String, ?> params)
		throws SQLException {
		return queryPageAsMap(getTransactionContext(sessionFactoryName), queryString, startNum, maxResults, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(Session session, String queryString, Object... params)
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
			return 0;
		} catch (HibernateException he) {
			throw new SQLException(he);
		}
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(HibernateTransactionContext context, String queryString, Object... params)
		throws SQLException {
		return queryCount(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String queryString, Object... params)
		throws SQLException {
		return queryCount(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String sessionFactoryName, String queryString, Object... params)
		throws SQLException {
		return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(Session session, String queryString, Iterable params)
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
	public static long queryCount(HibernateTransactionContext context, String queryString, Iterable params)
		throws SQLException {
		return queryCount(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String queryString, Iterable params)
		throws SQLException {
		return queryCount(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String sessionFactoryName, String queryString, Iterable params)
		throws SQLException {
		return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(Session session, String queryString, Map<String, ?> params)
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
	public static long queryCount(HibernateTransactionContext context, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryCount(getSession(context), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String queryString, Map<String, ?> params)
		throws SQLException {
		return queryCount(getTransactionContext(), queryString, params);
	}

	/**
	 * 根据 HQL 查询字符串和参数从数据库中获得整形返回值
	 */
	public static long queryCount(String sessionFactoryName, String queryString, Map<String, ?> params)
		throws SQLException {
		return queryCount(getTransactionContext(sessionFactoryName), queryString, params);
	}

	public static <T> T doReturningWork(Session session, ReturningWork<T> work)
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

	public static Integer updateSQL(Session session, final String sql, final Object... params)
		throws SQLException {
		return doReturningWork(session, new ReturningWork<Integer>() {
			@Override
			public Integer execute(Connection connection)
				throws SQLException {
				PreparedStatement stat = null;
				try {
					stat = connection.prepareStatement(sql);
					if (params != null) {
						for (int i = 0; i < params.length; i++) {
							stat.setObject(i + 1, params[i]);
						}
					}
					return stat.executeUpdate();
				} finally {
					if (stat != null) {
						stat.close();
					}
				}
			}
		});
	}

	public static Integer updateSQL(HibernateTransactionContext context, String sql, Object... params)
		throws SQLException {
		return updateSQL(getSession(context), sql, params);
	}

	public static Integer updateSQL(String sql, Object... params)
		throws SQLException {
		return updateSQL(getTransactionContext(), sql, params);
	}

	public static Integer updateSQL(String sessionFactoryName, String sql, Object... params)
		throws SQLException {
		return updateSQL(getTransactionContext(sessionFactoryName), sql, params);
	}

	public static List<Map<String, String>> querySQLAsMap(Session session, final String sql)
		throws SQLException {
		return doReturningWork(session, new ReturningWork<List<Map<String, String>>>() {
			@Override
			public List<Map<String, String>> execute(Connection connection)
				throws SQLException {
				Statement stmt = null;
				try {
					stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(sql);

					// field name
					ResultSetMetaData meta = rs.getMetaData();
					int count = meta.getColumnCount();
					String[] str = new String[count];
					for (int i = 0; i < count; i++) {
						str[i] = meta.getColumnName(i + 1).toLowerCase();
					}

					List<Map<String, String>> table = new ArrayList<Map<String, String>>();
					while (rs.next()) {
						Map<String, String> row = new HashMap<String, String>();
						for (int i = 0; i < count; i++) {
							row.put(str[i], rs.getString(i + 1));
						}
						table.add(row);
					}
					return table;
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
		});
	}

	public static List<Map<String, String>> querySQLAsMap(HibernateTransactionContext context, String sql)
		throws SQLException {
		return querySQLAsMap(getSession(context), sql);
	}

	public static List<Map<String, String>> querySQLAsMap(String sql)
		throws SQLException {
		return querySQLAsMap(getTransactionContext(), sql);
	}

	public static List<Map<String, String>> querySQLAsMap(String sessionFactoryName, String sql)
		throws SQLException {
		return querySQLAsMap(getTransactionContext(sessionFactoryName), sql);
	}

	public static List<Map<String, String>> querySQLPageAsMap(Session session, final String sql, final int startNum, final int numPerPage)
		throws SQLException {
		return doReturningWork(session, new ReturningWork<List<Map<String, String>>>() {
			@Override
			public List<Map<String, String>> execute(Connection connection)
				throws SQLException {
				Statement stmt = null;
				try {
					stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(sql);

					ResultSetMetaData meta = rs.getMetaData();
					int count = meta.getColumnCount();
					int i = 0;
					String[] str = new String[count];
					for (; i < count; i++) {
						str[i] = meta.getColumnName(i + 1).toLowerCase();
					}

					List<Map<String, String>> table = new ArrayList<Map<String, String>>();
					int index = 0;
					while (index < startNum && rs.next()) {
						index++;
					}
					int j = 0;
					while (rs.next() && j < numPerPage) {
						Map<String, String> row = new HashMap<String, String>();
						for (i = 0; i < count; i++) {
							String tem = Objects2.isNull(rs.getString(i + 1), "");
							row.put(str[i], tem);
						}
						table.add(row);
						j += 1;
					}
					return table;
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
		});
	}

	public static List<Map<String, String>> querySQLPageAsMap(HibernateTransactionContext context, String sql, int startNum, int numPerPage)
		throws SQLException {
		return querySQLPageAsMap(getSession(context), sql, startNum, numPerPage);
	}

	public static List<Map<String, String>> querySQLPageAsMap(String sql, int startNum, int numPerPage)
		throws SQLException {
		return querySQLPageAsMap(getTransactionContext(), sql, startNum, numPerPage);
	}

	public static List<Map<String, String>> querySQLPageAsMap(String sessionFactoryName, String sql, int startNum, int numPerPage)
		throws SQLException {
		return querySQLPageAsMap(getTransactionContext(sessionFactoryName), sql, startNum, numPerPage);
	}

	public static Long querySQLCount(Session session, final String sql, final Object... params)
		throws SQLException {
		return doReturningWork(session, new ReturningWork<Long>() {
			@Override
			public Long execute(Connection connection)
				throws SQLException {
				PreparedStatement stat = null;
				try {
					stat = connection.prepareStatement(sql);
					if (params != null) {
						for (int i = 0; i < params.length; i++) {
							stat.setObject(i + 1, params[i]);
						}
					}
					ResultSet rlt = stat.executeQuery();
					if (!rlt.next()) {
						return 0L;
					}
					return rlt.getLong(1);
				} finally {
					if (stat != null) {
						stat.close();
					}
				}
			}
		});
	}

	public static Long querySQLCount(HibernateTransactionContext context, String sql, Object... params)
		throws SQLException {
		return querySQLCount(getSession(context), sql, params);
	}

	public static Long querySQLCount(String sql, Object... params)
		throws SQLException {
		return querySQLCount(getTransactionContext(), sql, params);
	}

	public static Long querySQLCount(String sessionFactoryName, String sql, Object... params)
		throws SQLException {
		return querySQLCount(getTransactionContext(sessionFactoryName), sql, params);
	}

	public static Long[] querySQLCount(Session session, final String sql, final int countNum, final Object... params)
		throws SQLException {
		return doReturningWork(session, new ReturningWork<Long[]>() {
			@Override
			public Long[] execute(Connection connection)
				throws SQLException {
				PreparedStatement stat = null;
				ResultSet rlt = null;
				try {
					stat = connection.prepareStatement(sql);
					if (params != null) {
						for (int i = 0; i < params.length; i++) {
							stat.setObject(i + 1, params[i]);
						}
					}
					rlt = stat.executeQuery();
					Long[] result = new Long[countNum];
					if (rlt.next()) {
						for (int i = 0; i < countNum; i++) {
							result[i] = rlt.getLong(i + 1);
						}
					} else {
						for (int i = 0; i < countNum; i++) {
							result[i] = 0L;
						}
					}
					return result;
				} finally {
					if (stat != null) {
						stat.close();
					}
				}
			}
		});
	}

	public static Long[] querySQLCount(HibernateTransactionContext context, String sql, int countNum, Object... params)
		throws SQLException {
		return querySQLCount(getSession(context), sql, countNum, params);
	}

	public static Long[] querySQLCount(String sql, int countNum, Object... params)
		throws SQLException {
		return querySQLCount(getTransactionContext(), sql, countNum, params);
	}

	public static Long[] querySQLCount(String sessionFactoryName, String sql, int countNum, Object... params)
		throws SQLException {
		return querySQLCount(getTransactionContext(sessionFactoryName), sql, countNum, params);
	}
}
