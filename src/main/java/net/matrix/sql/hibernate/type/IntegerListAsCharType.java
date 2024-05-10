/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;

/**
 * 将数据库中的字符字段映射为 Java 中的整形列表类型。
 */
public class IntegerListAsCharType
    extends AbstractParameterizedUserType<List<Integer>, String, IntegerListAsCharMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public List<Integer> nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor session, Object object)
        throws SQLException {
        List<Integer> list = super.nullSafeGet(resultSet, strings, session, object);
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor session)
        throws SQLException {
        if (value == null) {
            preparedStatement.setNull(index, Types.VARCHAR);
            return;
        }

        List<Integer> list = (List) value;
        if (list.isEmpty()) {
            preparedStatement.setNull(index, Types.VARCHAR);
            return;
        }

        super.nullSafeSet(preparedStatement, value, index, session);
    }

    @Override
    public void applyConfiguration(SessionFactory sessionFactory) {
        super.applyConfiguration(sessionFactory);

        String separator = null;
        if (getParameterValues() != null) {
            separator = getParameterValues().getProperty("separator");
        }
        if (separator == null) {
            separator = ConfigurationHelper.getProperty("separator");
        }
        if (separator == null) {
            separator = ",";
        }

        IntegerListAsCharMapper columnMapper = getColumnMapper();
        columnMapper.setSeparator(separator);
    }
}
