package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserActivationFixture;
import jp.mts.authaccess.domain.model.UserActivationId;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.authaccess.domain.model.UserStatus;
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
	@Injectable UserRepository userRepository;
	@Injectable AuthenticateService authenticateService;

	@Test
	public void test_register() {

		final User user = new UserFixture().get();
		new Expectations() {{
			authenticateService.createUser("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = user;
			userRepository.findById(new UserId("u01"));
				result = null;
			
			userRepository.save(user);
		}};
		
		User actual = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(actual, is(user));
	}

	@Test
	public void test_activateUser_success(
			@Mocked DomainCalendar domainCalendar) {
		String userId = "u01";
		String activationId = "activate01";
		User user = new UserFixture(userId)
			.setUserActivation(
				new UserActivationFixture(activationId)
					.setExpireTime(Dates.dateShortTime("2015/10/01 12:00"))
					.get())
			.get();
		
		new Expectations() {{
			userRepository.findByActivationId(new UserActivationId(activationId));
				result = user;

			domainCalendar.systemDate();
				result = Dates.dateShortTime("2015/10/01 12:00");
				
			userRepository.save(user);
		}};
		DomainObject.setDomainCalendar(domainCalendar);
		
		User activatedUser = target.activateUser(activationId);

		assertThat(activatedUser.status(), is(UserStatus.ACTIVE));
	}

	@Test
	public void test_activateUser__no_activation_found() {

		new Expectations() {{
			userRepository.findByActivationId(new UserActivationId("activate01"));
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
		User user = new UserFixture(userId)
			.setUserActivation(
				new UserActivationFixture(activationId)
					.setExpireTime(Dates.dateShortTime("2015/10/01 12:00"))
					.get())
			.get();
		
		new Expectations() {{
			userRepository.findByActivationId(new UserActivationId(activationId));
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
