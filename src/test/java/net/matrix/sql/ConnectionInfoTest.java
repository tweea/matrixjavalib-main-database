/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectionInfoTest {
    private String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";

    private String url = "jdbc:derby:memory:mx_base;create=true";

    @Test
    public void testConnectionInfo()
        throws SQLException {
        ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
        assertThat(info.getDriverClass()).isEqualTo(driverClass);
        assertThat(info.getUrl()).isEqualTo(url);
        assertThat(info.getUsername()).isEmpty();
        assertThat(info.getPassword()).isEmpty();
        assertThat(info.getDatabaseType()).isEqualTo("Apache Derby");
        assertThat(info.getDriverName()).isEqualTo("Apache Derby Embedded JDBC Driver");
    }

    @Test
    public void testGetConnection()
        throws SQLException {
        ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");

        try (Connection conn = info.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }
}
