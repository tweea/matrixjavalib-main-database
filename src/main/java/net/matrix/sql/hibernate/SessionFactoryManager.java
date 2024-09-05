/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.matrix.lang.Resettable;
import net.matrix.sql.ConnectionInfo;
import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * Hibernate 会话工厂管理器。
 */
public final class SessionFactoryManager
    implements Resettable {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryManager.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(SessionFactoryManager.class).useCurrentLocale();

    /**
     * 默认的实例名称。
     */
    public static final String DEFAULT_NAME = "";

    /**
     * 所有的实例。
     */
    private static final Map<String, SessionFactoryManager> INSTANCES = new ConcurrentHashMap<>();

    /**
     * 实例名称。
     */
    @Nonnull
    private final String name;

    /**
     * Hibernate 配置资源。
     */
    @Nullable
    private final String configResource;

    /**
     * Hibernate 服务注册表。
     */
    @Nullable
    private ServiceRegistry serviceRegistry;

    /**
     * Hibernate 会话工厂。
     */
    @Nullable
    private SessionFactory sessionFactory;

    /**
     * 线程内 Hibernate 事务上下文。
     */
    @Nonnull
    private final ThreadLocal<HibernateTransactionContext> threadContext;

    /**
     * 获取默认名称实例。
     *
     * @return 实例。
     */
    @Nonnull
    public static SessionFactoryManager getInstance() {
        return INSTANCES.computeIfAbsent(DEFAULT_NAME, SessionFactoryManager::new);
    }

    /**
     * 获取特定名称实例。
     *
     * @param name
     *     实例名称。
     * @return 实例。
     * @throws IllegalStateException
     *     实例名称未命名。
     */
    @Nonnull
    public static SessionFactoryManager getInstance(@Nonnull String name) {
        if (DEFAULT_NAME.equals(name)) {
            return getInstance();
        }

        SessionFactoryManager instance = INSTANCES.get(name);
        if (instance == null) {
            throw new IllegalStateException(RBMF.format("实例名称 {0} 未命名", name));
        }

        return instance;
    }

    /**
     * 判断实例名称是否已命名。
     *
     * @param name
     *     实例名称。
     * @return 是否已命名。
     */
    public static boolean isNamed(@Nonnull String name) {
        return INSTANCES.containsKey(name);
    }

    /**
     * 命名实例名称，使用默认配置资源。
     *
     * @param name
     *     实例名称。
     * @throws IllegalStateException
     *     实例名称已命名。
     */
    public static void nameInstance(@Nonnull String name) {
        if (isNamed(name)) {
            throw new IllegalStateException(RBMF.format("实例名称 {0} 已命名", name));
        }

        INSTANCES.computeIfAbsent(name, SessionFactoryManager::new);
    }

    /**
     * 命名实例名称，使用指定配置资源。
     *
     * @param name
     *     实例名称。
     * @param configResource
     *     Hibernate 配置资源。
     * @throws IllegalStateException
     *     实例名称已命名。
     */
    public static void nameInstance(@Nonnull String name, @Nullable String configResource) {
        if (isNamed(name)) {
            throw new IllegalStateException(RBMF.format("实例名称 {0} 已命名", name));
        }

        INSTANCES.computeIfAbsent(name, key -> new SessionFactoryManager(name, configResource));
    }

    /**
     * 重置所有实例。
     */
    public static void resetAll() {
        for (SessionFactoryManager instance : INSTANCES.values()) {
            instance.reset();
        }
    }

    /**
     * 清除所有实例。
     */
    public static void clearAll() {
        resetAll();
        INSTANCES.clear();
    }

    private SessionFactoryManager(@Nonnull String name) {
        this.name = name;
        this.configResource = null;
        this.threadContext = new ThreadLocal<>();
    }

    private SessionFactoryManager(@Nonnull String name, @Nullable String configResource) {
        this.name = name;
        this.configResource = configResource;
        this.threadContext = new ThreadLocal<>();
    }

    /**
     * 获取实例名称。
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * 获取 Hibernate 配置资源。
     */
    @Nullable
    public String getConfigResource() {
        return configResource;
    }

    @Override
    public void reset() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (HibernateException e) {
                LOG.error(RBMF.get("实例 {} 的 Hibernate 会话工厂关闭失败。"), name, e);
            } finally {
                sessionFactory = null;
            }
        }
        if (serviceRegistry != null) {
            try {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
            } finally {
                serviceRegistry = null;
            }
        }
    }

    /**
     * 获取 Hibernate 服务注册表。
     *
     * @return Hibernate 服务注册表。
     */
    @Nonnull
    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            if (configResource == null) {
                serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            } else {
                serviceRegistry = new StandardServiceRegistryBuilder().configure(configResource).build();
            }
        }
        return serviceRegistry;
    }

    /**
     * 获取 Hibernate 会话工厂。
     *
     * @return Hibernate 会话工厂。
     */
    @Nonnull
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new MetadataSources(getServiceRegistry()).buildMetadata().buildSessionFactory();
        }
        return sessionFactory;
    }

    /**
     * 使用 Hibernate 会话工厂建立 Hibernate 会话。
     *
     * @return Hibernate 会话。
     */
    @Nonnull
    public Session createSession() {
        return getSessionFactory().openSession();
    }

    /**
     * 获取当前线程的事务上下文，没有则建立。
     *
     * @return 事务上下文。
     */
    @Nonnull
    public HibernateTransactionContext getTransactionContext() {
        HibernateTransactionContext context = threadContext.get();
        if (context == null) {
            context = new HibernateTransactionContext(this);
            threadContext.set(context);
        }
        return context;
    }

    /**
     * 丢弃当前线程的事务上下文。
     */
    public void dropTransactionContext() {
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
     * 获取数据库连接信息。
     *
     * @return 数据库连接信息。
     * @throws SQLException
     *     获取失败。
     */
    @Nonnull
    public ConnectionInfo getConnectionInfo()
        throws SQLException {
        Map<String, Object> settings = getServiceRegistry().getService(ConfigurationService.class).getSettings();
        String url = (String) settings.get(AvailableSettings.JAKARTA_JDBC_URL);
        String user = (String) settings.get(AvailableSettings.JAKARTA_JDBC_USER);
        String password = (String) settings.get(AvailableSettings.JAKARTA_JDBC_PASSWORD);
        return new ConnectionInfo(url, user, password);
    }
}
