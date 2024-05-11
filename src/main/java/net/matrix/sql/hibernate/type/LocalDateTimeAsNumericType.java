/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDateTime;

import org.hibernate.SessionFactory;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;

/**
 * 将数据库中的数值字段映射为 Java 中的本地日期时间类型。
 */
public class LocalDateTimeAsNumericType
    extends AbstractParameterizedUserType<LocalDateTime, Long, LocalDateTimeAsNumericMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public void applyConfiguration(SessionFactory sessionFactory) {
        super.applyConfiguration(sessionFactory);

        String format = null;
        if (getParameterValues() != null) {
            format = getParameterValues().getProperty("format");
        }
        if (format == null) {
            format = ConfigurationHelper.getProperty("format");
        }
        if (format == null) {
            format = "yyyyMMddHHmmss";
        }

        LocalDateTimeAsNumericMapper columnMapper = getColumnMapper();
        columnMapper.setFormat(format);
    }
}
