/*
 * $Id: BooleanAsIntegerType.java 864 2014-01-21 07:40:02Z tweea@263.net $
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate.type;

import org.jadira.usertype.spi.shared.AbstractSingleColumnUserType;

/**
 * 将数据库中的整形值作为布尔值处理的类型。
 */
public class BooleanAsIntegerType
	extends AbstractSingleColumnUserType<Boolean, Integer, IntegerColumnBooleanMapper> {
	/**
	 * 序列化。
	 */
	private static final long serialVersionUID = -1033841989530038459L;
}
