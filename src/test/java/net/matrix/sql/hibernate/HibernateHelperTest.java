/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class HibernateHelperTest {
    @BeforeClass
    public static void setUpBeforeClass()
        throws Exception {
        SessionFactoryManager.getInstance();
    }

    @Test
    public void querySQLAsMap()
        throws Exception {
        List<Map<String, Object>> result = HibernateHelper.querySQLAsMap("VALUES ('abc'), ('123')");
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0)).containsEntry("1", "abc");
    }

    @Test
    public void querySQLPageAsMap()
        throws Exception {
        List<Map<String, Object>> result = HibernateHelper.querySQLPageAsMap("VALUES ('abc'), ('123')", 1, 1);
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0)).containsEntry("1", "123");
    }
}
