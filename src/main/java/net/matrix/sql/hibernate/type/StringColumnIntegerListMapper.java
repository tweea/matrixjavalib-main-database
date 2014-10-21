/*
 * $Id: StringColumnIntegerListMapper.java 867 2014-01-21 10:25:12Z tweea@263.net $
 * 版权所有 2014 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;

/**
 * 映射整形列表值到字符串字段。
 */
public class StringColumnIntegerListMapper
	extends AbstractStringColumnMapper<List<Integer>> {
	private static final long serialVersionUID = -3448788221055335510L;

	private String separator;

	private Pattern pattern;

	public void setSeparator(String separator) {
		this.separator = separator;
		this.pattern = Pattern.compile(separator);
	}

	@Override
	public List<Integer> fromNonNullValue(String value) {
		List<Integer> result = new ArrayList<Integer>();
		String[] list = pattern.split(value);
		for (String item : list) {
			result.add(Integer.parseInt(item));
		}
		return result;
	}

	@Override
	public String toNonNullValue(List<Integer> value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.size(); i++) {
			if (i != 0) {
				sb.append(separator);
			}
			sb.append(value.get(i));
		}
		return sb.toString();
	}
}
