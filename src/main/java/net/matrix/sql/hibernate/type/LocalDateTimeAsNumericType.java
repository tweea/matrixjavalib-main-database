/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDateTime;
import java.util.Properties;

import net.matrix.sql.hibernate.type.jadira.AbstractSingleColumnUserType;

/**
 * 将数据库中的数值字段映射为 Java 中的本地日期时间类型。
 */
public class LocalDateTimeAsNumericType
    extends AbstractSingleColumnUserType<LocalDateTime, Long, LocalDateTimeAsNumericMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public void setParameterValues(Properties parameters) {
        super.setParameterValues(parameters);

        String pattern = null;
        if (parameters != null) {
            pattern = parameters.getProperty("pattern");
        }
        if (pattern == null) {
            pattern = "yyyyMMddHHmmss";
        }

        LocalDateTimeAsNumericMapper columnMapper = getColumnMapper();
        columnMapper.setPattern(pattern);
    }
}
