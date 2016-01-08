package jp.mts.base.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void test() {
		Map<String, Integer> map = MapUtils.pairs("b", 2, "a", 1);
		
		assertThat(map.get("a"), is(1));
		assertThat(map.get("b"), is(2));

		assertThat(map.entrySet().stream().findFirst().get().getKey() , is("a"));
	}
	@Test
	public void test_empty() {
		Map<String, Integer> map = MapUtils.pairs();
		
		assertThat(map.isEmpty(), is(true));
	}
	@Test(expected=IllegalArgumentException.class)
	public void test_invalid_argment_count() {
		MapUtils.pairs("a",1,"b");
	}

}
