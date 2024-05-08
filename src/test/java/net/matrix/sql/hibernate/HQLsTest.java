/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HQLsTest {
    @Test
    public void testAppendParameterName() {
        StringBuilder hql = new StringBuilder();

        HQLs.appendParameterName(hql, 0);
        assertThat(hql.toString()).isEqualTo(":p0");
    }

    @Test
    public void testGetParameterName() {
        assertThat(HQLs.getParameterName(0)).isEqualTo("p0");
        assertThat(HQLs.getParameterName(10)).isEqualTo("p10");
    }
}
