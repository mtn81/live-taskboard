package jp.mts.authaccess.infrastructure.service.socialauth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.authaccess.infrastructure.service.socialauth.QueryParameters;

import org.junit.Test;

public class QueryParametersTest {

	@Test
	public void test() {
		QueryParameters target = new QueryParameters("a=b&c=d");
		assertThat(target.get("a"), is("b"));
		assertThat(target.get("c"), is("d"));
	}

}
