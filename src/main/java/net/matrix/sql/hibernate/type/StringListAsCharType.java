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
 * 将数据库中的字符字段映射为 Java 中的字符串列表类型。
 */
public class StringListAsCharType
    extends AbstractSingleColumnUserType<List<String>, String, StringListAsCharMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object object)
        throws SQLException {
        List<String> list = super.nullSafeGet(resultSet, position, session, object);
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, List<String> value, int index, SharedSessionContractImplementor session)
        throws SQLException {
        if (value.isEmpty()) {
            value = null;
        }

        super.nullSafeSet(preparedStatement, value, index, session);
    }

    @Override
    public List<String> deepCopy(List<String> value) {
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

        StringListAsCharMapper columnMapper = getColumnMapper();
        columnMapper.setSeparator(separator);
    }
}
