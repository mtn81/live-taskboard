package jp.mts.base.util;


public class Assertions {

	public static void assertNonNull(Object arg) {
		if(arg == null) throw new IllegalArgumentException("must not be null");
	}
}
