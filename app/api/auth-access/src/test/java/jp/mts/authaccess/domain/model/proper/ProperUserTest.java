package jp.mts.authaccess.domain.model.proper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.UserChanged;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

public class ProperUserTest {

	@Test public void 
	test_changeAttributes(@Mocked DomainEventPublisher domainEventPublisher) {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		ProperUser properUser = new ProperUserFixture().get();
		
		//execute
		properUser.changeAttributes("new name", "new email", true);
		
		//verify
		assertThat(properUser.name(), is("new name"));
		assertThat(properUser.email(), is("new email"));
		assertThat(properUser.useEmailNotification(), is(true));
		
		new Verifications() {{
			UserChanged event;
			domainEventPublisher.publish(event = withCapture());
			
			assertThat(event.getUserId(), is("u01"));
			assertThat(event.getName(), is("new name"));
			assertThat(event.getEmail(), is("new email"));
			assertThat(event.getNotifyEmail(), is(true));
		}};
		
	}

}
