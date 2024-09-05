/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jakarta.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * Hibernate 事务上下文。
 */
public class HibernateTransactionContext {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(HibernateTransactionContext.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(HibernateTransactionContext.class).useCurrentLocale();

    /**
     * Hibernate 会话工厂管理器。
     */
    @Nonnull
    private final SessionFactoryManager sessionFactoryManager;

    /**
     * Hibernate 会话。
     */
    @Nullable
    private Session session;

    /**
     * Hibernate 事务。
     */
    @Nullable
    private Transaction transaction;

    /**
     * 构造器，使用指定 Hibernate 会话工厂管理器。
     *
     * @param sessionFactoryManager
     *     Hibernate 会话工厂管理器。
     */
    public HibernateTransactionContext(@Nonnull SessionFactoryManager sessionFactoryManager) {
        this.sessionFactoryManager = sessionFactoryManager;
    }

    /**
     * 获取 Hibernate 会话。
     *
     * @return Hibernate 会话。
     */
    @Nonnull
    public Session getSession() {
        if (session == null) {
            session = sessionFactoryManager.createSession();
        }
        return session;
    }

    /**
     * 启动事务。
     */
    public void begin() {
        if (transaction == null) {
            transaction = getSession().beginTransaction();
        }
    }

    /**
     * 提交事务。
     */
    public void commit() {
        if (transaction == null) {
            return;
        }

        try {
            transaction.commit();
        } finally {
            transaction = null;
        }
    }

    /**
     * 撤销事务。
     */
    public void rollback() {
        if (transaction == null) {
            return;
        }

        try {
            transaction.rollback();
        } finally {
            transaction = null;
        }
    }

    /**
     * 释放事务资源。
     */
    public void release() {
        if (transaction != null) {
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (PersistenceException e) {
                LOG.warn(RBMF.get("撤销 Hibernate 事务失败"), e);
            } finally {
                transaction = null;
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException e) {
                LOG.warn(RBMF.get("关闭 Hibernate 会话失败"), e);
            } finally {
                session = null;
            }
        }
    }
}
