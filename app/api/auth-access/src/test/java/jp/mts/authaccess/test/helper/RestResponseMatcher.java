package jp.mts.authaccess.test.helper;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jp.mts.authaccess.rest.RestResponse;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class RestResponseMatcher extends BaseMatcher<RestResponse<?>> {

	private Set<String> errorCodes = new HashSet<>();
	
	@Override
	public boolean matches(Object item) {
		RestResponse<?> response = (RestResponse<?>)item;
		return errorCodes.stream().allMatch(errorCode -> {
			return response.hasError(errorCode);
		});
	}

	@Override
	public void describeTo(Description description) {
		
	}
	
	public static RestResponseMatcher hasError(String... errorCodes){
		RestResponseMatcher matcher = new RestResponseMatcher();
		matcher.errorCodes.addAll(Arrays.asList(errorCodes));
		return matcher;
	}

}
