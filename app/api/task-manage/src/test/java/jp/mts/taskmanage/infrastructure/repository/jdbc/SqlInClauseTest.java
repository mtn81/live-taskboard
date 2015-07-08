package jp.mts.taskmanage.infrastructure.repository.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

public class SqlInClauseTest {

	@Test
	public void test() {
		SqlInClause<String> target = new SqlInClause<>(
				"col", Lists.newArrayList("a","b","c"), param -> "test" + param);

		assertThat(target.condition(), is("col in (?,?,?)"));
		assertThat(target.params(), is(new Object[]{"testa","testb","testc"}));
	}

}
