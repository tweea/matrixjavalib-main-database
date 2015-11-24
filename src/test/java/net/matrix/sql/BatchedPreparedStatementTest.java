/*
 * 版权所有 2013 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BatchedPreparedStatementTest {
	@Mock
	private PreparedStatement statement;

	private FieldSupport fieldSupport = FieldSupport.extraction();

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBatchedPreparedStatementPreparedStatement() {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement);
		Assertions.assertThat(fieldSupport.fieldValue("statement", PreparedStatement.class, batchedStatement)).isSameAs(statement);
		Assertions.assertThat(fieldSupport.fieldValue("batchSize", Integer.class, batchedStatement)).isEqualTo(0);
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		Assertions.assertThat(fieldSupport.fieldValue("batchResult", int[].class, batchedStatement)).isEmpty();
	}

	@Test
	public void testBatchedPreparedStatementPreparedStatementInt() {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 3);
		Assertions.assertThat(fieldSupport.fieldValue("statement", PreparedStatement.class, batchedStatement)).isSameAs(statement);
		Assertions.assertThat(fieldSupport.fieldValue("batchSize", Integer.class, batchedStatement)).isEqualTo(3);
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		Assertions.assertThat(fieldSupport.fieldValue("batchResult", int[].class, batchedStatement)).isEmpty();
	}

	@Test
	public void testAddBatch()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		batchedStatement.addBatch();
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(1);
		batchedStatement.addBatch();
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
	}

	@Test
	public void testClearBatch()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		batchedStatement.addBatch();
		batchedStatement.clearBatch();
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
	}

	@Test
	public void testClose()
		throws SQLException {
		try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2)) {
			Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
			batchedStatement.addBatch();
			batchedStatement.close();
			Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		}
	}

	@Test
	public void testExecuteBatch()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Mockito.when(statement.executeBatch()).thenReturn(new int[] {
			1, 2
		}, new int[] {
			3
		});
		Assertions.assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
		batchedStatement.addBatch();
		batchedStatement.addBatch();
		batchedStatement.addBatch();
		Assertions.assertThat(batchedStatement.executeBatch()).containsExactly(1, 2, 3);
	}

	@Test
	public void testIsWrapperFor()
		throws SQLException {
		try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement)) {
			Assertions.assertThat(batchedStatement.isWrapperFor(Integer.class)).isFalse();
			Assertions.assertThat(batchedStatement.isWrapperFor(PreparedStatement.class)).isTrue();
		}
	}

	@Test
	public void testUnwrap()
		throws SQLException {
		try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement)) {
			Assertions.assertThat(batchedStatement.unwrap(PreparedStatement.class)).isSameAs(statement);
		}
	}
}
