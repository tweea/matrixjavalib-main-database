/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HQLmxTest {
    @Test
    void testAppendParameterName() {
        StringBuilder hql = new StringBuilder();

        HQLmx.appendParameterName(hql, 0);
        assertThat(hql).hasToString(":p0");
    }

    @Test
    void testGetParameterName() {
        assertThat(HQLmx.getParameterName(0)).isEqualTo("p0");
        assertThat(HQLmx.getParameterName(10)).isEqualTo("p10");
    }
}
