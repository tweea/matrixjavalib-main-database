/*
 * 版权所有 2014 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import org.jadira.usertype.spi.shared.AbstractIntegerColumnMapper;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * 映射日期时间值到整形字段。
 */
public class IntegerColumnLocalDateTimeMapper
	extends AbstractIntegerColumnMapper<LocalDateTime> {
	private static final long serialVersionUID = -3448788221055335510L;

	/**
	 * 日期格式。
	 */
	private String format;

	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	public LocalDateTime fromNonNullValue(final Integer value) {
		return fromNonNullString(value.toString());
	}

	@Override
	public Integer toNonNullValue(final LocalDateTime value) {
		return Integer.valueOf(toNonNullString(value));
	}

	@Override
	public LocalDateTime fromNonNullString(final String s) {
		return DateTimeFormat.forPattern(format).parseLocalDateTime(s);
	}

	@Override
	public String toNonNullString(final LocalDateTime value) {
		return DateTimeFormat.forPattern(format).print(value);
	}
}
