/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.sql.SQLException;
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
    public void testQuerySQLAsMap()
        throws SQLException {
        List<Map<String, Object>> result = HibernateHelper.querySQLAsMap("VALUES ('abc'), ('123')");
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("1", "abc");
    }

    @Test
    public void testQuerySQLPageAsMap()
        throws SQLException {
        List<Map<String, Object>> result = HibernateHelper.querySQLPageAsMap("VALUES ('abc'), ('123')", 1, 1);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsEntry("1", "123");
    }
}
