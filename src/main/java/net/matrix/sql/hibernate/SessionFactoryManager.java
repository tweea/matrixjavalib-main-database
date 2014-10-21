/*
 * $Id: SessionFactoryManager.java 901 2014-01-22 16:36:15Z tweea@263.net $
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.matrix.lang.Resettable;
import net.matrix.sql.ConnectionInfo;

/**
 * Hibernate SessionFactory 管理器。
 */
public final class SessionFactoryManager
	implements Resettable {
	/**
	 * 默认的 SessionFactory 名称。
	 */
	public static final String DEFAULT_NAME = "";

	/**
	 * 日志记录器。
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryManager.class);

	/**
	 * 所有的实例。
	 */
	private static final Map<String, SessionFactoryManager> INSTANCES = new HashMap<String, SessionFactoryManager>();

	private final String factoryName;

	private final String configResource;

	private final ThreadLocal<HibernateTransactionContext> threadContext;

	private Configuration configuration;

	private ServiceRegistry serviceRegistry;

	private SessionFactory sessionFactory;

	/**
	 * @return 默认实例
	 */
	public static SessionFactoryManager getInstance() {
		SessionFactoryManager instance = INSTANCES.get(DEFAULT_NAME);
		if (instance != null) {
			return instance;
		}
		synchronized (INSTANCES) {
			instance = INSTANCES.get(DEFAULT_NAME);
			if (instance == null) {
				instance = new SessionFactoryManager(DEFAULT_NAME);
				INSTANCES.put(DEFAULT_NAME, instance);
			}
		}
		return instance;
	}

	/**
	 * @param name
	 *            SessionFactory 名称
	 * @return 实例
	 * @throws IllegalStateException
	 *             还未命名实例
	 */
	public static SessionFactoryManager getInstance(String name) {
		if (DEFAULT_NAME.equals(name)) {
			return getInstance();
		}
		if (!isNameUsed(name)) {
			throw new IllegalStateException("名称 " + name + " 没有命名");
		}
		return INSTANCES.get(name);
	}

	/**
	 * 判断 SessionFactory 名称是否已被占用。
	 * 
	 * @param name
	 *            SessionFactory 名称
	 * @return true 为已占用
	 */
	public static boolean isNameUsed(String name) {
		return INSTANCES.containsKey(name);
	}

	/**
	 * 命名默认配置文件到指定名称。
	 * 
	 * @param name
	 *            SessionFactory 名称
	 * @throws IllegalStateException
	 *             名称已被占用
	 */
	public static void nameSessionFactory(String name) {
		synchronized (INSTANCES) {
			if (isNameUsed(name)) {
				throw new IllegalStateException("名称 " + name + " 已被占用");
			}
			INSTANCES.put(name, new SessionFactoryManager(name));
		}
	}

	/**
	 * 命名一个配置文件到指定名称。
	 * 
	 * @param name
	 *            SessionFactory 名称
	 * @param configResource
	 *            SessionFactory 配置资源
	 * @throws IllegalStateException
	 *             名称已被占用
	 */
	public static void nameSessionFactory(String name, String configResource) {
		synchronized (INSTANCES) {
			if (isNameUsed(name)) {
				throw new IllegalStateException("名称 " + name + " 已被占用");
			}
			INSTANCES.put(name, new SessionFactoryManager(name, configResource));
		}
	}

	/**
	 * 清除所有 SessionFactory 配置。
	 */
	public static void clearAll() {
		resetAll();
		INSTANCES.clear();
	}

	/**
	 * 重置所有 SessionFactory 配置。
	 */
	public static void resetAll() {
		for (SessionFactoryManager instance : INSTANCES.values()) {
			instance.reset();
		}
	}

	private SessionFactoryManager(String name) {
		this.factoryName = name;
		this.configResource = null;
		this.threadContext = new ThreadLocal<HibernateTransactionContext>();
	}

	private SessionFactoryManager(String name, String configResource) {
		this.factoryName = name;
		this.configResource = configResource;
		this.threadContext = new ThreadLocal<HibernateTransactionContext>();
	}

	/**
	 * 关闭 SessionFactory。
	 * 
	 * @see net.matrix.lang.Resettable#reset()
	 */
	@Override
	public void reset() {
		if (sessionFactory != null) {
			try {
				sessionFactory.close();
				LOG.info(factoryName + " 配置的 Hibernate SessionFactory 已关闭。");
			} catch (HibernateException e) {
				LOG.error(factoryName + " 配置的 Hibernate SessionFactory 关闭失败。");
			} finally {
				sessionFactory = null;
			}
		}
		if (serviceRegistry != null) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
			LOG.info(factoryName + " 配置的 Hibernate ServiceRegistry 已销毁。");
			serviceRegistry = null;
		}
		configuration = null;
	}

	/**
	 * 获取 SessionFactory 配置。
	 * 
	 * @return SessionFactory 配置
	 */
	public Configuration getConfiguration()
		throws SQLException {
		try {
			if (configuration == null) {
				if (configResource == null) {
					LOG.info("读取默认的 Hibernate 配置。");
					configuration = new Configuration().configure();
				} else {
					LOG.info("读取 " + configResource + "的 Hibernate 配置。");
					configuration = new Configuration().configure(configResource);
				}
			}
			return configuration;
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 获取 ServiceRegistry。
	 * 
	 * @return ServiceRegistry
	 */
	public ServiceRegistry getServiceRegistry()
		throws SQLException {
		try {
			if (serviceRegistry == null) {
				if (DEFAULT_NAME.equals(factoryName)) {
					LOG.info("以默认配置构建 Hibernate ServiceRegistry。");
				} else {
					LOG.info("以 " + factoryName + " 配置构建 Hibernate ServiceRegistry。");
				}
				serviceRegistry = new StandardServiceRegistryBuilder().applySettings(getConfiguration().getProperties()).build();
			}
			return serviceRegistry;
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 获取 SessionFactory。
	 * 
	 * @return SessionFactory
	 */
	public SessionFactory getSessionFactory()
		throws SQLException {
		try {
			if (sessionFactory == null) {
				if (DEFAULT_NAME.equals(factoryName)) {
					LOG.info("以默认配置构建 Hibernate SessionFactory。");
				} else {
					LOG.info("以 " + factoryName + " 配置构建 Hibernate SessionFactory。");
				}
				sessionFactory = getConfiguration().buildSessionFactory(getServiceRegistry());
			}
			return sessionFactory;
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 使用 SessionFactory 建立 Session。
	 * 
	 * @return 新建的 Session
	 * @throws SQLException
	 *             建立失败
	 */
	public Session createSession()
		throws SQLException {
		try {
			return getSessionFactory().openSession();
		} catch (HibernateException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 获取当前顶层事务上下文，没有则建立。
	 * 
	 * @return 当前顶层事务上下文
	 */
	public HibernateTransactionContext getTransactionContext() {
		HibernateTransactionContext context = threadContext.get();
		if (context == null) {
			context = new HibernateTransactionContext(factoryName);
			threadContext.set(context);
		}
		return context;
	}

	/**
	 * 丢弃顶层事务上下文。
	 * 
	 * @throws SQLException
	 *             回滚发生错误
	 */
	public void dropTransactionContext()
		throws SQLException {
		HibernateTransactionContext context = threadContext.get();
		if (context == null) {
			return;
		}
		threadContext.remove();
		try {
			context.rollback();
		} finally {
			context.release();
		}
	}

	/**
	 * 获取 SessionFactory 相关连接信息。
	 * 
	 * @return 连接信息
	 * @throws SQLException
	 *             信息获取失败
	 */
	public ConnectionInfo getConnectionInfo()
		throws SQLException {
		Properties properties = getConfiguration().getProperties();
		String driver = properties.getProperty(AvailableSettings.DRIVER);
		String url = properties.getProperty(AvailableSettings.URL);
		String user = properties.getProperty(AvailableSettings.USER);
		String pass = properties.getProperty(AvailableSettings.PASS);
		return new ConnectionInfo(driver, url, user, pass);
	}
}
