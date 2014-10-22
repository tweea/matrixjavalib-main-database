/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

/**
 * HQL 工具类。
 */
public final class HQLs {
	private static final char[] PARAMETER_PREFIX = {
		':', 'p'
	};

	/**
	 * 阻止实例化。
	 */
	private HQLs() {
	}

	public static void appendParameterName(StringBuilder sb, int index) {
		sb.append(PARAMETER_PREFIX).append(index);
	}

	public static String getParameterName(int index) {
		return PARAMETER_PREFIX[1] + Integer.toString(index);
	}
}
