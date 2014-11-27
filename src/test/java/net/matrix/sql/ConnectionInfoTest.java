/*
 * 版权所有 2013 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ConnectionInfoTest {
	private String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";

	private String url = "jdbc:derby:memory:mx_base;create=true";

	@Test
	public void testConnectionInfo()
		throws SQLException {
		ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
		Assertions.assertThat(info.getDriverClass()).isEqualTo(driverClass);
		Assertions.assertThat(info.getUrl()).isEqualTo(url);
		Assertions.assertThat(info.getUsername()).isEmpty();
		Assertions.assertThat(info.getPassword()).isEmpty();
		Assertions.assertThat(info.getDatabaseType()).isEqualTo("Apache Derby");
		Assertions.assertThat(info.getDriverName()).isEqualTo("Apache Derby Embedded JDBC Driver");
	}

	@Test
	public void testGetConnection()
		throws SQLException {
		ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
		try (Connection conn = info.getConnection()) {
			Assertions.assertThat(conn).isNotNull();
		}
	}
}
