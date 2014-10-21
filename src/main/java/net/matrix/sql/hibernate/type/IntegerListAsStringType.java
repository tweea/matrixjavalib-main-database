/*
 * $Id: IntegerListAsStringType.java 867 2014-01-21 10:25:12Z tweea@263.net $
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;

/**
 * 将数据库中的字符串值作为整形列表值处理的类型。
 */
public class IntegerListAsStringType
	extends AbstractParameterizedUserType<List<Integer>, String, StringColumnIntegerListMapper> {
	/**
	 * 序列化。
	 */
	private static final long serialVersionUID = 2068406929466129144L;

	@Override
	public List<Integer> nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
		throws HibernateException, SQLException {
		List<Integer> list = super.nullSafeGet(rs, names, session, owner);
		if (list == null) {
			return new ArrayList();
		}
		return list;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
		throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
			return;
		}
		List<Integer> v = (List) value;
		if (v.isEmpty()) {
			st.setNull(index, Types.VARCHAR);
			return;
		}
		super.nullSafeSet(st, value, index, session);
	}

	@Override
	public void applyConfiguration(SessionFactory sessionFactory) {
		super.applyConfiguration(sessionFactory);

		StringColumnIntegerListMapper columnMapper = getColumnMapper();

		String separator = null;
		if (getParameterValues() != null) {
			separator = getParameterValues().getProperty("separator");
		}
		if (separator == null) {
			separator = ConfigurationHelper.getProperty("separator");
		}

		if (separator == null) {
			columnMapper.setSeparator(",");
		} else {
			columnMapper.setSeparator(separator);
		}
	}
}
