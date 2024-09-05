/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import net.matrix.sql.hibernate.type.jadira.AbstractSingleColumnUserType;

/**
 * 将数据库中的字符字段映射为 Java 中的整型列表类型。
 */
public class IntegerListAsCharType
    extends AbstractSingleColumnUserType<List<Integer>, String, IntegerListAsCharMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public List<Integer> nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object object)
        throws SQLException {
        List<Integer> list = super.nullSafeGet(resultSet, position, session, object);
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, List<Integer> value, int index, SharedSessionContractImplementor session)
        throws SQLException {
        if (value.isEmpty()) {
            value = null;
        }

        super.nullSafeSet(preparedStatement, value, index, session);
    }

    @Override
    public List<Integer> deepCopy(List<Integer> value) {
        if (value == null) {
            return null;
        }

        return new ArrayList(value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        super.setParameterValues(parameters);

        String separator = null;
        if (parameters != null) {
            separator = parameters.getProperty("separator");
        }
        if (separator == null) {
            separator = ",";
        }

        IntegerListAsCharMapper columnMapper = getColumnMapper();
        columnMapper.setSeparator(separator);
    }
}
