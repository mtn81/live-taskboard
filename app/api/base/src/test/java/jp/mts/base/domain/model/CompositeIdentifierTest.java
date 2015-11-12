package jp.mts.base.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.junit.Test;

public class CompositeIdentifierTest {

	@Test
	public void test() {
		CompositeIdentifier id1 = CompositeIdentifier.of(new TestId("a"), new TestId("b"));
		CompositeIdentifier id2 = CompositeIdentifier.of(new TestId("a"), new TestId("b"));
		CompositeIdentifier id3 = CompositeIdentifier.of(new TestId("b"), new TestId("a"));
		
		assertThat(id1, is(id2));
		assertThat(id1, is(not(id3)));
	}
	
	public static class TestId extends Identifier<String> {
		public TestId(String value) {
			super(value);
		}
	}

}
