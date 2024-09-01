/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 包装数据库预编译语句，限定批量执行的最大数量，超过最大数量后自动提交到数据库执行。
 */
public class BatchedPreparedStatement
    implements PreparedStatement {
    /**
     * 被包装的数据库预编译语句。
     */
    private final PreparedStatement statement;

    /**
     * 批量执行的最大数量。
     */
    private final int batchSize;

    /**
     * 等待批量执行的数量。
     */
    private int batchCount;

    /**
     * 批量执行的结果。
     */
    private long[] batchResult;

    /**
     * 构造器，指定被包装的数据库预编译语句和批量执行的最大数量。
     *
     * @param statement
     *     被包装的数据库预编译语句。
     * @param batchSize
     *     批量执行的最大数量。
     */
    public BatchedPreparedStatement(@Nonnull PreparedStatement statement, int batchSize) {
        this.statement = statement;
        this.batchSize = batchSize;
        this.batchCount = 0;
        this.batchResult = ArrayUtils.EMPTY_LONG_ARRAY;
    }

    /**
     * 检查等待批量执行的数量是否已达到最大数量。
     */
    private boolean checkBatchCount() {
        return batchSize > 0 && batchCount >= batchSize;
    }

    /**
     * 等待批量执行的数量加一。
     */
    private void addBatchCount() {
        if (batchSize > 0) {
            ++batchCount;
        }
    }

    /**
     * 等待批量执行的数量清零。
     */
    private void resetBatchCount() {
        batchCount = 0;
    }

    /**
     * 记录批量执行的结果。
     *
     * @param result
     *     批量执行的结果。
     */
    private void addBatchResult(long[] result) {
        batchResult = ArrayUtils.addAll(batchResult, result);
    }

    /**
     * 重置批量执行的结果。
     */
    private void resetBatchResult() {
        batchResult = ArrayUtils.EMPTY_LONG_ARRAY;
    }

    private int[] toIntArray(long[] longArray) {
        int[] intArray = new int[longArray.length];
        for (int index = 0; index < longArray.length; ++index) {
            intArray[index] = (int) longArray[index];
        }
        return intArray;
    }

    private long[] toLongArray(int[] intArray) {
        long[] longArray = new long[intArray.length];
        for (int index = 0; index < intArray.length; ++index) {
            longArray[index] = intArray[index];
        }
        return longArray;
    }

    @Override
    public ResultSet executeQuery()
        throws SQLException {
        return statement.executeQuery();
    }

    @Override
    public int executeUpdate()
        throws SQLException {
        return statement.executeUpdate();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType)
        throws SQLException {
        statement.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x)
        throws SQLException {
        statement.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x)
        throws SQLException {
        statement.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x)
        throws SQLException {
        statement.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x)
        throws SQLException {
        statement.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x)
        throws SQLException {
        statement.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x)
        throws SQLException {
        statement.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x)
        throws SQLException {
        statement.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x)
        throws SQLException {
        statement.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x)
        throws SQLException {
        statement.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x)
        throws SQLException {
        statement.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x)
        throws SQLException {
        statement.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x)
        throws SQLException {
        statement.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x)
        throws SQLException {
        statement.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
        throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
    }

    @Deprecated
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
        throws SQLException {
        statement.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
        throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters()
        throws SQLException {
        statement.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType)
        throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x)
        throws SQLException {
        statement.setObject(parameterIndex, x);
    }

    @Override
    public boolean execute()
        throws SQLException {
        return statement.execute();
    }

    @Override
    public void addBatch()
        throws SQLException {
        statement.addBatch();
        addBatchCount();
        if (checkBatchCount()) {
            int[] result = statement.executeBatch();
            addBatchResult(toLongArray(result));
            resetBatchCount();
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
        throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x)
        throws SQLException {
        statement.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x)
        throws SQLException {
        statement.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x)
        throws SQLException {
        statement.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x)
        throws SQLException {
        statement.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData()
        throws SQLException {
        return statement.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal)
        throws SQLException {
        statement.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal)
        throws SQLException {
        statement.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
        throws SQLException {
        statement.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName)
        throws SQLException {
        statement.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x)
        throws SQLException {
        statement.setURL(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData()
        throws SQLException {
        return statement.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x)
        throws SQLException {
        statement.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value)
        throws SQLException {
        statement.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length)
        throws SQLException {
        statement.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value)
        throws SQLException {
        statement.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length)
        throws SQLException {
        statement.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
        throws SQLException {
        statement.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length)
        throws SQLException {
        statement.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
        throws SQLException {
        statement.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
        throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
        throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
        throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length)
        throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x)
        throws SQLException {
        statement.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x)
        throws SQLException {
        statement.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader)
        throws SQLException {
        statement.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value)
        throws SQLException {
        statement.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader)
        throws SQLException {
        statement.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream)
        throws SQLException {
        statement.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader)
        throws SQLException {
        statement.setNClob(parameterIndex, reader);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)
        throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType)
        throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public long executeLargeUpdate()
        throws SQLException {
        return statement.executeLargeUpdate();
    }

    @Override
    public ResultSet executeQuery(String sql)
        throws SQLException {
        return statement.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql)
        throws SQLException {
        return statement.executeUpdate(sql);
    }

    @Override
    public void close()
        throws SQLException {
        statement.close();
        resetBatchCount();
        resetBatchResult();
    }

    @Override
    public int getMaxFieldSize()
        throws SQLException {
        return statement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max)
        throws SQLException {
        statement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows()
        throws SQLException {
        return statement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max)
        throws SQLException {
        statement.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable)
        throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout()
        throws SQLException {
        return statement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds)
        throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel()
        throws SQLException {
        statement.cancel();
    }

    @Override
    public SQLWarning getWarnings()
        throws SQLException {
        return statement.getWarnings();
    }

    @Override
    public void clearWarnings()
        throws SQLException {
        statement.clearWarnings();
    }

    @Override
    public void setCursorName(String name)
        throws SQLException {
        statement.setCursorName(name);
    }

    @Override
    public boolean execute(String sql)
        throws SQLException {
        return statement.execute(sql);
    }

    @Override
    public ResultSet getResultSet()
        throws SQLException {
        return statement.getResultSet();
    }

    @Override
    public int getUpdateCount()
        throws SQLException {
        return statement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults()
        throws SQLException {
        return statement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction)
        throws SQLException {
        statement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection()
        throws SQLException {
        return statement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows)
        throws SQLException {
        statement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize()
        throws SQLException {
        return statement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency()
        throws SQLException {
        return statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType()
        throws SQLException {
        return statement.getResultSetType();
    }

    @Override
    public void addBatch(String sql)
        throws SQLException {
        // 这里会抛出异常
        statement.addBatch(sql);
    }

    @Override
    public void clearBatch()
        throws SQLException {
        statement.clearBatch();
        resetBatchCount();
        resetBatchResult();
    }

    @Override
    public int[] executeBatch()
        throws SQLException {
        int[] result = statement.executeBatch();
        addBatchResult(toLongArray(result));
        result = toIntArray(batchResult);
        resetBatchCount();
        resetBatchResult();
        return result;
    }

    @Override
    public Connection getConnection()
        throws SQLException {
        return statement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current)
        throws SQLException {
        return statement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys()
        throws SQLException {
        return statement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        return statement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        return statement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException {
        return statement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        return statement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException {
        return statement.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        return statement.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability()
        throws SQLException {
        return statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed()
        throws SQLException {
        return statement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable)
        throws SQLException {
        statement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable()
        throws SQLException {
        return statement.isPoolable();
    }

    @Override
    public void closeOnCompletion()
        throws SQLException {
        statement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion()
        throws SQLException {
        return statement.isCloseOnCompletion();
    }

    @Override
    public long getLargeUpdateCount()
        throws SQLException {
        return statement.getLargeUpdateCount();
    }

    @Override
    public void setLargeMaxRows(long max)
        throws SQLException {
        statement.setLargeMaxRows(max);
    }

    @Override
    public long getLargeMaxRows()
        throws SQLException {
        return statement.getLargeMaxRows();
    }

    @Override
    public long[] executeLargeBatch()
        throws SQLException {
        long[] result = statement.executeLargeBatch();
        addBatchResult(result);
        result = batchResult;
        resetBatchCount();
        resetBatchResult();
        return result;
    }

    @Override
    public long executeLargeUpdate(String sql)
        throws SQLException {
        return statement.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        return statement.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        return statement.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames)
        throws SQLException {
        return statement.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException {
        if (iface.isAssignableFrom(statement.getClass())) {
            return (T) statement;
        }
        return statement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException {
        if (iface.isAssignableFrom(statement.getClass())) {
            return true;
        }
        return statement.isWrapperFor(iface);
    }
}
