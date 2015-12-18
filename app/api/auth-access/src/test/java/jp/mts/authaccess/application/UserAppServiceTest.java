package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationId;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationFixture;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

public class UserAppServiceTest {

	@Tested UserAppService target = new UserAppService();
	@Injectable ProperUserRepository userRepository;
	@Injectable ProperAuthenticateService authenticateService;

	@Test
	public void test_register() {

		final ProperUser user = new ProperUserFixture().get();
		new Expectations() {{
			authenticateService.createUser("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = user;
			userRepository.findById(new ProperUserId("u01"));
				result = null;
			
			userRepository.save(user);
		}};
		
		ProperUser actual = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(actual, is(user));
	}

	@Test
	public void test_activateUser_success(
			@Mocked DomainCalendar domainCalendar) {
		String userId = "u01";
		String activationId = "activate01";
		ProperUser user = new ProperUserFixture(userId)
			.setUserActivation(
				new ProperUserActivationFixture(activationId)
					.setExpireTime(Dates.dateShortTime("2015/10/01 12:00"))
					.get())
			.get();
		
		new Expectations() {{
			userRepository.findByActivationId(new ProperUserActivationId(activationId));
				result = user;

			domainCalendar.systemDate();
				result = Dates.dateShortTime("2015/10/01 12:00");
				
			userRepository.save(user);
		}};
		DomainObject.setDomainCalendar(domainCalendar);
		
		ProperUser activatedUser = target.activateUser(activationId);

		assertThat(activatedUser.status(), is(ProperUserStatus.ACTIVE));
	}

	@Test
	public void test_activateUser__no_activation_found() {

		new Expectations() {{
			userRepository.findByActivationId(new ProperUserActivationId("activate01"));
				result = null;
		}};
		
		try {
			target.activateUser("activate01");
		} catch (ApplicationException e) {
			assertThat(e.hasErrorOf(ErrorType.ACTIVATION_NOT_FOUND), is(true));
		}
	}
		
	@Test
	public void test_activateUser__activation_expired(
			@Mocked DomainCalendar domainCalendar) {
		
		String userId = "u01";
		String activationId = "activate01";
		ProperUser user = new ProperUserFixture(userId)
			.setUserActivation(
				new ProperUserActivationFixture(activationId)
					.setExpireTime(Dates.dateShortTime("2015/10/01 12:00"))
					.get())
			.get();
		
		new Expectations() {{
			userRepository.findByActivationId(new ProperUserActivationId(activationId));
				result = user;

			domainCalendar.systemDate();
				result = Dates.dateShortTime("2015/10/01 12:01");
				
		}};
		DomainObject.setDomainCalendar(domainCalendar);
		
		try {
			target.activateUser(activationId);
		} catch (ApplicationException e) {
			assertThat(e.hasErrorOf(ErrorType.ACTIVATION_EXPIRED), is(true));
		}
	}
}
