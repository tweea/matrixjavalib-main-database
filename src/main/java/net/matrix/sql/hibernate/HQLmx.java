/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * HQL 工具。
 */
@ThreadSafe
public final class HQLmx {
    private static final char[] PARAMETER_PREFIX = {
        ':', 'p'
    };

    /**
     * 阻止实例化。
     */
    private HQLmx() {
    }

    /**
     * 在 HQL 中拼入参数名，格式为“:p”加序号。
     * 
     * @param hql
     *     HQL。
     * @param index
     *     序号。
     */
    public static void appendParameterName(@Nonnull StringBuilder hql, int index) {
        hql.append(PARAMETER_PREFIX).append(index);
    }

    /**
     * 生成参数名，格式为“p”加序号。
     * 
     * @param index
     *     序号。
     */
    @Nonnull
    public static String getParameterName(int index) {
        return PARAMETER_PREFIX[1] + Integer.toString(index);
    }
}
