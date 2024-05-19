/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;

import net.matrix.java.lang.ObjectMx;

/**
 * 将数据库中的字符字段映射为 Java 中的整型列表类型。
 */
public class IntegerListAsCharMapper
    extends AbstractStringColumnMapper<List<Integer>> {
    private static final long serialVersionUID = 1L;

    /**
     * 分隔符。
     */
    private String separator;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public List<Integer> fromNonNullValue(String value) {
        String[] valueParts = StringUtils.splitByWholeSeparator(value, separator);

        List<Integer> list = new ArrayList<>(valueParts.length);
        for (String valuePart : valueParts) {
            valuePart = StringUtils.trimToNull(valuePart);
            list.add(ObjectMx.ifNotNullMap(valuePart, Integer::valueOf));
        }
        return list;
    }

    @Override
    public String toNonNullValue(List<Integer> value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            Integer valuePart = value.get(i);

            if (i != 0) {
                sb.append(separator);
            }
            if (valuePart != null) {
                sb.append(valuePart);
            }
        }
        return sb.toString();
    }
}
