/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalTime;

import javax.annotation.Nonnull;

import net.matrix.java.time.DateTimeFormatterMx;
import net.matrix.sql.hibernate.type.jadira.AbstractLongColumnMapper;

/**
 * 将数据库中的数值字段映射为 Java 中的本地时间类型。
 */
public class LocalTimeAsNumericMapper
    extends AbstractLongColumnMapper<LocalTime> {
    private static final long serialVersionUID = 1L;

    /**
     * 日期格式。
     */
    @Nonnull
    private String pattern;

    public void setPattern(@Nonnull String pattern) {
        this.pattern = pattern;
    }

    @Override
    public LocalTime fromNonNullValue(Long value) {
        return fromNonNullString(value.toString());
    }

    @Override
    public LocalTime fromNonNullString(CharSequence s) {
        return DateTimeFormatterMx.parseLocalTime(s, pattern);
    }

    @Override
    public Long toNonNullValue(LocalTime value) {
        return Long.valueOf(toNonNullString(value));
    }

    @Override
    public String toNonNullString(LocalTime value) {
        return DateTimeFormatterMx.format(value, pattern);
    }
}
