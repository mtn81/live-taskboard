package jp.mts.authaccess.infrastructure.service;

import static org.junit.Assert.*;
import jp.mts.authaccess.domain.model.social.SocialUser;

import org.junit.Test;

public class OpenIdConnectSocialAuthDomainServiceTest {

	@Test
	public void test_loadSocialUser() {
		
		OpenIdConnectSocialAuthDomainService target = new OpenIdConnectSocialAuthDomainService();
		
		SocialUser actual = target.loadSocialUser("auth01");
		
	}

}
