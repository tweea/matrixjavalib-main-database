/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.sql.hibernate;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Hibernate 测试
 */
public class HQLsTest {
	@Test
	public void getParameterName()
		throws Exception {
		Assertions.assertThat(HQLs.getParameterName(0)).isEqualTo("p0");
		Assertions.assertThat(HQLs.getParameterName(10)).isEqualTo("p10");
	}
}
