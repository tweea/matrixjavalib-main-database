/*
 * 版权所有 2013 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.assertj.core.util.introspection.FieldSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchedPreparedStatementTest {
    private FieldSupport fieldSupport = FieldSupport.extraction();

    private AutoCloseable mock;

    @Mock
    private PreparedStatement statement;

    @BeforeEach
    public void beforeEach() {
        mock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void afterEach()
        throws Exception {
        mock.close();
    }

    @Test
    public void testBatchedPreparedStatement() {
        PreparedStatement batchedStatement = new BatchedPreparedStatement(statement);
        assertThat(fieldSupport.fieldValue("statement", PreparedStatement.class, batchedStatement)).isSameAs(statement);
        assertThat(fieldSupport.fieldValue("batchSize", Integer.class, batchedStatement)).isEqualTo(0);
        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
        assertThat(fieldSupport.fieldValue("batchResult", long[].class, batchedStatement)).isEmpty();
    }

    @Test
    public void testBatchedPreparedStatement_withSize() {
        PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 3);
        assertThat(fieldSupport.fieldValue("statement", PreparedStatement.class, batchedStatement)).isSameAs(statement);
        assertThat(fieldSupport.fieldValue("batchSize", Integer.class, batchedStatement)).isEqualTo(3);
        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
        assertThat(fieldSupport.fieldValue("batchResult", long[].class, batchedStatement)).isEmpty();
    }

    @Test
    public void testAddBatch()
        throws SQLException {
        PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);
        Mockito.when(statement.executeBatch()).thenReturn(new int[] {
            1, 2
        });

        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
        batchedStatement.addBatch();
        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(1);
        batchedStatement.addBatch();
        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
    }

    @Test
    public void testClearBatch()
        throws SQLException {
        PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2);

        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
        batchedStatement.addBatch();
        batchedStatement.clearBatch();
        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
    }

    @Test
    public void testClose()
        throws SQLException {
        try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement, 2)) {
            assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
            batchedStatement.addBatch();
            batchedStatement.close();
            assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
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

        assertThat(fieldSupport.fieldValue("batchCount", Integer.class, batchedStatement)).isEqualTo(0);
        batchedStatement.addBatch();
        batchedStatement.addBatch();
        batchedStatement.addBatch();
        assertThat(batchedStatement.executeBatch()).containsExactly(1, 2, 3);
    }

    @Test
    public void testIsWrapperFor()
        throws SQLException {
        try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement)) {
            assertThat(batchedStatement.isWrapperFor(Integer.class)).isFalse();
            assertThat(batchedStatement.isWrapperFor(PreparedStatement.class)).isTrue();
        }
    }

    @Test
    public void testUnwrap()
        throws SQLException {
        try (PreparedStatement batchedStatement = new BatchedPreparedStatement(statement)) {
            assertThat(batchedStatement.unwrap(PreparedStatement.class)).isSameAs(statement);
        }
    }
}
