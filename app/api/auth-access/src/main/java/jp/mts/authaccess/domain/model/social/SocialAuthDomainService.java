package jp.mts.authaccess.domain.model.social;

import java.util.HashMap;
import java.util.Map;

import jp.mts.authaccess.domain.model.UserType;

public class SocialAuthDomainService {

	private Map<UserType, SocialAuthProvider> providers = new HashMap<>();

	public SocialAuthProvider providerOf(UserType userType) {
		if(!providers.containsKey(userType)) 
			throw new IllegalArgumentException();
		return providers.get(userType);
	}
	
	public SocialAuthDomainService addProvider(SocialAuthProvider provider) {
		providers.put(provider.targetUserType(), provider);
		return this;
	}

}
