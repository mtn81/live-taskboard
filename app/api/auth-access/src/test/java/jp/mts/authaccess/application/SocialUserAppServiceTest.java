package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.authaccess.domain.model.UserChanged;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserFixture;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.domain.model.social.SocialUserRepository;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;


public class SocialUserAppServiceTest {

	@Tested SocialUserAppService target = new SocialUserAppService();
	@Injectable SocialUserRepository socialUserRepository;
	
	@Test public void 
	test_loadUser() {
		
		SocialUser socialUser = new SocialUserFixture().get();
		
		new Expectations() {{
			socialUserRepository.findById(new SocialUserId(UserType.GOOGLE, "u01"));
				result = Optional.of(socialUser);
		}};
		
		SocialUser actual = target.loadUser("GOOGLE_u01");
		
		assertThat(actual, is(sameInstance(socialUser)));
	}

	@Test public void 
	test_changeUserAttributes(@Mocked DomainEventPublisher domainEventPublisher) {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		SocialUser existSocialUser = new SocialUserFixture().get();
		
		new Expectations(existSocialUser) {{
			socialUserRepository.findById(new SocialUserId(UserType.GOOGLE, "u01"));
				result = Optional.of(existSocialUser);
				
			existSocialUser.changeAttributes("taro", "hoge@test.jp", true);
				
			socialUserRepository.save(existSocialUser);
		}};
		
		SocialUser savedUser = target.changeUserAttributes("GOOGLE_u01", "taro", "hoge@test.jp", true);
		
		assertThat(savedUser, is(sameInstance(existSocialUser)));

	}
}
