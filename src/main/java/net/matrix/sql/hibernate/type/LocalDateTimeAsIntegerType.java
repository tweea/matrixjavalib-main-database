/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate.type;

import org.hibernate.SessionFactory;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.ConfigurationHelper;
import org.joda.time.LocalDateTime;

/**
 * 将数据库中的整形值作为日期时间值处理的类型。
 */
public class LocalDateTimeAsIntegerType
    extends AbstractParameterizedUserType<LocalDateTime, Integer, IntegerColumnLocalDateTimeMapper> {
    /**
     * 序列化。
     */
    private static final long serialVersionUID = -460554393165063180L;

    /**
     * 仅有日期的格式。
     */
    public static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public void applyConfiguration(final SessionFactory sessionFactory) {
        super.applyConfiguration(sessionFactory);

        IntegerColumnLocalDateTimeMapper columnMapper = getColumnMapper();

        String format = null;
        if (getParameterValues() != null) {
            format = getParameterValues().getProperty("format");
        }
        if (format == null) {
            format = ConfigurationHelper.getProperty("format");
        }

        if (format == null) {
            columnMapper.setFormat(DATE_FORMAT);
        } else {
            columnMapper.setFormat(format);
        }
    }
}
