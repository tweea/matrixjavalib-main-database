/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hibernate 测试
 */
public class HQLsTest {
    @Test
    public void testGetParameterName() {
        assertThat(HQLs.getParameterName(0)).isEqualTo("p0");
        assertThat(HQLs.getParameterName(10)).isEqualTo("p10");
    }
}
