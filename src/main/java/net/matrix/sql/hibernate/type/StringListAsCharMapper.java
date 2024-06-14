/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;

/**
 * 将数据库中的字符字段映射为 Java 中的字符串列表类型。
 */
public class StringListAsCharMapper
    extends AbstractStringColumnMapper<List<String>> {
    private static final long serialVersionUID = 1L;

    /**
     * 分隔符。
     */
    @Nonnull
    private String separator;

    public void setSeparator(@Nonnull String separator) {
        this.separator = separator;
    }

    @Override
    public List<String> fromNonNullValue(String value) {
        String[] valueParts = StringUtils.splitByWholeSeparator(value, separator);

        List<String> list = new ArrayList<>(valueParts.length);
        Collections.addAll(list, valueParts);
        return list;
    }

    @Override
    public String toNonNullValue(List<String> value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            String valuePart = value.get(i);

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
