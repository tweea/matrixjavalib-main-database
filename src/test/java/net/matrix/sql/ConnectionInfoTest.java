/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionInfoTest {
    String url = "jdbc:derby:memory:mx_base;create=true";

    @Test
    void testNew()
        throws SQLException {
        ConnectionInfo info = new ConnectionInfo(url, "", "");
        assertThat(info.getUrl()).isEqualTo(url);
        assertThat(info.getUser()).isEmpty();
        assertThat(info.getPassword()).isEmpty();
        assertThat(info.getDatabaseProductName()).isEqualTo("Apache Derby");
        assertThat(info.getDriverName()).isEqualTo("Apache Derby Embedded JDBC Driver");
    }

    @Test
    void testGetConnection()
        throws SQLException {
        ConnectionInfo info = new ConnectionInfo(url, "", "");

        try (Connection conn = info.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }
}
