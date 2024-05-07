/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 事务上下文。
 */
public class HibernateTransactionContext {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateTransactionContext.class);

    private final String sessionFactoryName;

    private Session session;

    private Transaction transaction;

    /**
     * 构建使用默认 {@link SessionFactory} 的实例。
     */
    public HibernateTransactionContext() {
        this.sessionFactoryName = SessionFactoryManager.DEFAULT_NAME;
    }

    /**
     * 构建使用指定 {@link SessionFactory} 的实例。
     * 
     * @param sessionFactoryName
     *     {@link SessionFactory} 名称
     */
    public HibernateTransactionContext(final String sessionFactoryName) {
        if (sessionFactoryName == null) {
            this.sessionFactoryName = SessionFactoryManager.DEFAULT_NAME;
        } else {
            this.sessionFactoryName = sessionFactoryName;
        }
    }

    /**
     * 获取对应的 Hibernate {@link Session}。
     * 
     * @return Hibernate {@link Session}
     * @throws SQLException
     *     建立 {@link Session} 失败
     */
    public Session getSession()
        throws SQLException {
        if (session == null) {
            try {
                session = SessionFactoryManager.getInstance(sessionFactoryName).createSession();
            } catch (HibernateException ex) {
                throw new SQLException(ex);
            }
        }
        return session;
    }

    /**
     * 启动事务。
     * 
     * @throws SQLException
     *     启动失败
     */
    public void begin()
        throws SQLException {
        if (transaction == null) {
            try {
                transaction = getSession().beginTransaction();
            } catch (HibernateException ex) {
                throw new SQLException(ex);
            }
        }
    }

    /**
     * 提交事务。
     * 
     * @throws SQLException
     *     提交失败
     */
    public void commit()
        throws SQLException {
        if (transaction != null) {
            try {
                transaction.commit();
                transaction = null;
            } catch (HibernateException ex) {
                throw new SQLException(ex);
            }
        }
    }

    /**
     * 撤销事务。
     */
    public void rollback() {
        if (transaction != null) {
            try {
                transaction.rollback();
            } finally {
                transaction = null;
            }
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
            } catch (HibernateException ex) {
                LOG.warn("Hibernate 事务回滚失败", ex);
            } finally {
                transaction = null;
            }
        }
        if (session != null) {
            try {
                session.close();
                LOG.debug("Hibernate 会话结束");
            } catch (HibernateException ex) {
                LOG.warn("Hibernate 会话结束失败", ex);
            } finally {
                session = null;
            }
        }
    }
}
