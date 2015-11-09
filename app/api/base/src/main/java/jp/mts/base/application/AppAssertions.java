package jp.mts.base.application;


public class AppAssertions {
	
	public static <T> void assertTrue(Predicate predicate, ErrorType errorType) {
		if(!predicate.test()) {
			throw new ApplicationException(errorType);
		}
	}
	public static <T> void assertTrue(boolean isTrue, ErrorType errorType) {
		if(!isTrue) {
			throw new ApplicationException(errorType);
		}
	}
	
	@FunctionalInterface
	public interface Predicate {
		boolean test();
	}
	
}
