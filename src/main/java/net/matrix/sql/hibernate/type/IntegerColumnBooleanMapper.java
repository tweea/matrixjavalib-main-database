/*
 * 版权所有 2014 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import org.jadira.usertype.spi.shared.AbstractIntegerColumnMapper;

/**
 * 映射布尔值到整形字段。
 */
public class IntegerColumnBooleanMapper
	extends AbstractIntegerColumnMapper<Boolean> {
	private static final long serialVersionUID = 4205713919952452881L;

	@Override
	public Boolean fromNonNullValue(final Integer i) {
		if (i == 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public Integer toNonNullValue(final Boolean value) {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Boolean fromNonNullString(final String s) {
		if ("0".equals(s)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public String toNonNullString(final Boolean value) {
		if (value) {
			return "1";
		} else {
			return "0";
		}
	}
}
