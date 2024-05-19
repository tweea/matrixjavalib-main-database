/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.time.LocalTime;

import org.hibernate.SessionFactory;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;

/**
 * 将数据库中的数值字段映射为 Java 中的本地时间类型。
 */
public class LocalTimeAsNumericType
    extends AbstractParameterizedUserType<LocalTime, Long, LocalTimeAsNumericMapper> {
    private static final long serialVersionUID = 1L;

    @Override
    public void applyConfiguration(SessionFactory sessionFactory) {
        super.applyConfiguration(sessionFactory);

        String pattern = null;
        if (getParameterValues() != null) {
            pattern = getParameterValues().getProperty("pattern");
        }
        if (pattern == null) {
            pattern = ConfigurationHelper.getProperty("pattern");
        }
        if (pattern == null) {
            pattern = "HHmmss";
        }

        LocalTimeAsNumericMapper columnMapper = getColumnMapper();
        columnMapper.setPattern(pattern);
    }
}
