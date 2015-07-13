package jp.mts.base.unittest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jp.mts.base.rest.RestResponse;

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
