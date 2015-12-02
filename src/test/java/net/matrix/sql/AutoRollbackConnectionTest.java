/*
 * 版权所有 2015 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class AutoRollbackConnectionTest {
	@Test
	public void testClose()
		throws SQLException {
		Connection connection = Mockito.mock(Connection.class);
		try (Connection autoRollbackConnection = new AutoRollbackConnection(connection)) {
		}
		Mockito.verify(connection).isClosed();
		Mockito.verify(connection).getAutoCommit();
		Mockito.verify(connection).rollback();
		Mockito.verify(connection).close();
		Mockito.verifyNoMoreInteractions(connection);
	}

	@Test(expected = SQLException.class)
	public void testCloseRollbackFail()
		throws SQLException {
		Connection connection = Mockito.mock(Connection.class);
		Mockito.doThrow(SQLException.class).when(connection).rollback();
		try (Connection autoRollbackConnection = new AutoRollbackConnection(connection)) {
		}
		Mockito.verify(connection).isClosed();
		Mockito.verify(connection).getAutoCommit();
		Mockito.verify(connection).rollback();
		Mockito.verify(connection).close();
		Mockito.verifyNoMoreInteractions(connection);
	}

	@Test
	public void testCloseAutoCommit()
		throws SQLException {
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(connection.getAutoCommit()).thenReturn(true);
		try (Connection autoRollbackConnection = new AutoRollbackConnection(connection)) {
		}
		Mockito.verify(connection).isClosed();
		Mockito.verify(connection).getAutoCommit();
		Mockito.verify(connection).close();
		Mockito.verifyNoMoreInteractions(connection);
	}

	@Test
	public void testCloseClosed()
		throws SQLException {
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(connection.isClosed()).thenReturn(true);
		try (Connection autoRollbackConnection = new AutoRollbackConnection(connection)) {
		}
		Mockito.verify(connection).isClosed();
		Mockito.verifyNoMoreInteractions(connection);
	}

	@Test
	public void testCloseReal()
		throws SQLException {
		String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";
		String url = "jdbc:derby:memory:mx_base;create=true";
		ConnectionInfo info = new ConnectionInfo(driverClass, url, "", "");
		Connection connection = info.getConnection();
		try (Connection autoRollbackConnection = new AutoRollbackConnection(connection)) {
			PreparedStatement ps = autoRollbackConnection.prepareStatement("VALUES CURRENT_DATE");
			ResultSet rs = ps.executeQuery();
			Assertions.assertThat(rs.next()).isTrue();
			Assertions.assertThat(rs.next()).isFalse();
		}
	}
}
