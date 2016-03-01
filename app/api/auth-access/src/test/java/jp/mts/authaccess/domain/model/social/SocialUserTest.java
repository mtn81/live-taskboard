package jp.mts.authaccess.domain.model.social;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.authaccess.domain.model.UserChanged;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

public class SocialUserTest {

	@Test public void 
	test_email() {
		SocialUser socialUser = new SocialUserFixture().get();
		assertThat(socialUser.email(), is("social@test.jp"));

		SocialUser socialUser2 = new SocialUserFixture().setCustomEmail("social2@test.jp").get();
		assertThat(socialUser2.email(), is("social2@test.jp"));
	}

	@Test public void 
	test_name() {
		SocialUser socialUser = new SocialUserFixture().get();
		assertThat(socialUser.name(), is("taro"));

		SocialUser socialUser2 = new SocialUserFixture().setCustomName("taro2").get();
		assertThat(socialUser2.name(), is("taro2"));
	}
	
	@Test public void
	test_changeAttributes(@Mocked DomainEventPublisher domainEventPublisher) {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);

		//execute
		SocialUser socialUser = new SocialUserFixture().get();
		socialUser.changeAttributes("new name", "new email", true);

		//verify
		assertThat(socialUser.name(), is("new name"));
		assertThat(socialUser.email(), is("new email"));
		assertThat(socialUser.useEmailNotification(), is(true));
		
		new Verifications() {{
			UserChanged event;
			domainEventPublisher.publish(event = withCapture()); times=1;
			assertThat(event.getUserId(), is("GOOGLE_u01"));
			assertThat(event.getName(), is("new name"));
			assertThat(event.getEmail(), is("new email"));
			assertThat(event.getNotifyEmail(), is(true));
		}};
		
	}

}
