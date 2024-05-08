/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HibernateHelperTest {
    @BeforeAll
    public static void beforeAll() {
        SessionFactoryManager.getInstance();
    }

    @Test
    public void testQuerySQLAsMap() {
        List<Map<String, Object>> result = HibernateHelper.querySQLAsMap("VALUES ('abc'), ('123')");
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("1", "abc");
    }

    @Test
    public void testQuerySQLPageAsMap() {
        List<Map<String, Object>> result = HibernateHelper.querySQLPageAsMap("VALUES ('abc'), ('123')", 1, 1);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsEntry("1", "123");
    }
}
