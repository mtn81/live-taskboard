package jp.mts.base.aspect;

public class ClientContext {

	private static final ThreadLocal<String> value = new ThreadLocal<String>(){
		@Override
		protected String initialValue() {
			return null;
		}
	};
	public static String clientId() {
		return value.get();
	}
	public static void start(String clientId) {
		value.set(clientId);
	}
}
