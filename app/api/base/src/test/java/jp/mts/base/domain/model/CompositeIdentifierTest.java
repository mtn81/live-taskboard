package jp.mts.base.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.junit.Test;

public class CompositeIdentifierTest {

	@Test
	public void test() {
		CompositeId id1 = CompositeId.of(new TestId("a"), new TestId("b"));
		CompositeId id2 = CompositeId.of(new TestId("a"), new TestId("b"));
		CompositeId id3 = CompositeId.of(new TestId("b"), new TestId("a"));
		
		assertThat(id1, is(id2));
		assertThat(id1, is(not(id3)));
	}
	
	public static class TestId extends DomainId<String> {
		public TestId(String value) {
			super(value);
		}
	}

}
