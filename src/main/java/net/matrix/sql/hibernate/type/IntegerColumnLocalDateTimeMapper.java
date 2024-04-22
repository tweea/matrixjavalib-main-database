/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDateTime;

import org.jadira.usertype.spi.shared.AbstractIntegerColumnMapper;

import net.matrix.java.time.DateTimeFormatterMx;

/**
 * 映射日期时间值到整形字段。
 */
public class IntegerColumnLocalDateTimeMapper
    extends AbstractIntegerColumnMapper<LocalDateTime> {
    private static final long serialVersionUID = 1L;

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
        return DateTimeFormatterMx.parseLocalDateTime(s, format);
    }

    @Override
    public String toNonNullString(final LocalDateTime value) {
        return DateTimeFormatterMx.format(value, format);
    }
}
