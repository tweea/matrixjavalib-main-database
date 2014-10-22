/*
 * 版权所有 2013 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionInfoTest {
	private String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";

	private String url = "jdbc:derby:memory:mx_base;create=true";

	@Test
	public void testConnectionInfo()
		throws SQLException {
		ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
		Assert.assertEquals(driverClass, info.getDriverClass());
		Assert.assertEquals(url, info.getUrl());
		Assert.assertEquals("", info.getUsername());
		Assert.assertEquals("", info.getPassword());
		Assert.assertEquals("Apache Derby", info.getDatabaseType());
		Assert.assertEquals("Apache Derby Embedded JDBC Driver", info.getDriverName());
	}

	@Test
	public void testGetConnection()
		throws SQLException {
		ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
		Connection conn = info.getConnection();
		Assert.assertNotNull(conn);
		conn.close();
	}
}
