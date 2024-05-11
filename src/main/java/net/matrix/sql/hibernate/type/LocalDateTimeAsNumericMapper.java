/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDateTime;

import org.jadira.usertype.spi.shared.AbstractLongColumnMapper;

import net.matrix.java.time.DateTimeFormatterMx;

/**
 * 将数据库中的数值字段映射为 Java 中的本地日期时间类型。
 */
public class LocalDateTimeAsNumericMapper
    extends AbstractLongColumnMapper<LocalDateTime> {
    private static final long serialVersionUID = 1L;

    /**
     * 日期格式。
     */
    private String format;

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public LocalDateTime fromNonNullValue(Long value) {
        return fromNonNullString(value.toString());
    }

    @Override
    public LocalDateTime fromNonNullString(String s) {
        return DateTimeFormatterMx.parseLocalDateTime(s, format);
    }

    @Override
    public Long toNonNullValue(LocalDateTime value) {
        return Long.valueOf(toNonNullString(value));
    }

    @Override
    public String toNonNullString(LocalDateTime value) {
        return DateTimeFormatterMx.format(value, format);
    }
}
