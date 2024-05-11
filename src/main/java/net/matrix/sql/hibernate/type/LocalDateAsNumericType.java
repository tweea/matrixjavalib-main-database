/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalDate;

import org.hibernate.SessionFactory;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;

/**
 * 将数据库中的数值字段映射为 Java 中的本地日期类型。
 */
public class LocalDateAsNumericType
    extends AbstractParameterizedUserType<LocalDate, Long, LocalDateAsNumericMapper> {
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
            format = "yyyyMMdd";
        }

        LocalDateAsNumericMapper columnMapper = getColumnMapper();
        columnMapper.setFormat(format);
    }
}
