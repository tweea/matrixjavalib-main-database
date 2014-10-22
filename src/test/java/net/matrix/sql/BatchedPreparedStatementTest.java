/*
 * 版权所有 2013 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;
import org.fest.reflect.core.Reflection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BatchedPreparedStatementTest {
	@Mock
	private PreparedStatement statement;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBatchedPreparedStatementPreparedStatement() {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement);
		Assert.assertSame(statement, Reflection.field("statement").ofType(PreparedStatement.class).in(batchedStatement).get());
		Assert.assertEquals(0, Reflection.field("batchSize").ofType(int.class).in(batchedStatement).get().intValue());
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		Assert.assertArrayEquals(ArrayUtils.EMPTY_INT_ARRAY, Reflection.field("batchResult").ofType(int[].class).in(batchedStatement).get());
	}

	@Test
	public void testBatchedPreparedStatementPreparedStatementInt() {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 3);
		Assert.assertSame(statement, Reflection.field("statement").ofType(PreparedStatement.class).in(batchedStatement).get());
		Assert.assertEquals(3, Reflection.field("batchSize").ofType(int.class).in(batchedStatement).get().intValue());
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		Assert.assertArrayEquals(ArrayUtils.EMPTY_INT_ARRAY, Reflection.field("batchResult").ofType(int[].class).in(batchedStatement).get());
	}

	@Test
	public void testAddBatch()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		batchedStatement.addBatch();
		Assert.assertEquals(1, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		batchedStatement.addBatch();
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
	}

	@Test
	public void testClearBatch()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		batchedStatement.addBatch();
		batchedStatement.clearBatch();
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
	}

	@Test
	public void testClose()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		batchedStatement.addBatch();
		batchedStatement.close();
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
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
		Assert.assertEquals(0, Reflection.field("batchCount").ofType(int.class).in(batchedStatement).get().intValue());
		batchedStatement.addBatch();
		batchedStatement.addBatch();
		batchedStatement.addBatch();
		Assert.assertArrayEquals(new int[] {
			1, 2, 3
		}, batchedStatement.executeBatch());
	}

	@Test
	public void testIsWrapperFor()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement);
		Assert.assertFalse(batchedStatement.isWrapperFor(Integer.class));
		Assert.assertTrue(batchedStatement.isWrapperFor(PreparedStatement.class));
		batchedStatement.close();
	}

	@Test
	public void testUnwrap()
		throws SQLException {
		PreparedStatement batchedStatement = new BatchedPreparedStatement(statement);
		Assert.assertSame(statement, batchedStatement.unwrap(PreparedStatement.class));
		batchedStatement.close();
	}
}
