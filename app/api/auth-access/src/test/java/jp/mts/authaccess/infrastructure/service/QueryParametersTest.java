package jp.mts.authaccess.infrastructure.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class QueryParametersTest {

	@Test
	public void test() {
		QueryParameters target = new QueryParameters("a=b&c=d");
		assertThat(target.get("a"), is("b"));
		assertThat(target.get("c"), is("d"));
	}

}
