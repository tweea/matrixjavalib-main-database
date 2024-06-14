/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * 数据库连接信息。
 */
@Immutable
public class ConnectionInfo {
    // 连接信息
    /**
     * JDBC 连接地址。
     */
    private final String url;

    /**
     * 用户。
     */
    private final String user;

    /**
     * 密码。
     */
    private final String password;

    // 元数据
    /**
     * 数据库产品名称。
     */
    private String databaseProductName;

    /**
     * JDBC 驱动名称。
     */
    private String driverName;

    /**
     * 构造器，连接数据库获取元数据。
     * 
     * @param url
     *     JDBC 连接地址。
     * @param user
     *     用户。
     * @param password
     *     密码。
     * @throws SQLException
     *     获取元数据失败。
     */
    public ConnectionInfo(@Nonnull String url, @Nullable String user, @Nullable String password)
        throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        readMetaData();
    }

    /**
     * 连接数据库获取元数据。
     * 
     * @throws SQLException
     *     获取元数据失败。
     */
    private void readMetaData()
        throws SQLException {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            databaseProductName = metaData.getDatabaseProductName();
            driverName = metaData.getDriverName();
        }
    }

    /**
     * 获取 JDBC 连接地址。
     */
    @Nonnull
    public String getUrl() {
        return url;
    }

    /**
     * 获取用户。
     */
    @Nullable
    public String getUser() {
        return user;
    }

    /**
     * 获取密码。
     */
    @Nullable
    public String getPassword() {
        return password;
    }

    /**
     * 获取数据库产品名称。
     */
    @Nonnull
    public String getDatabaseProductName() {
        return databaseProductName;
    }

    /**
     * 获取 JDBC 驱动名称。
     */
    @Nonnull
    public String getDriverName() {
        return driverName;
    }

    /**
     * 建立数据库连接。
     * 
     * @return 数据库连接。
     * @throws SQLException
     *     建立数据库连接失败。
     */
    @Nonnull
    public Connection getConnection()
        throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
