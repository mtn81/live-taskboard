package jp.mts.authaccess.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class AuthenticateServiceTest {

	@Mocked	UserRepository userRepository;
	@Mocked	AuthRepository authRepository;
	@Mocked PasswordEncriptionService userPasswordEncriptinService;
	@Mocked DomainEventPublisher domainEventPublisher;
	
	@Test
	public void test() {
		AuthenticateService target = new AuthenticateService(
				userRepository, authRepository, userPasswordEncriptinService, domainEventPublisher);

		final UserId userId = new UserId("u01");
		final AuthId authId = new AuthId("a01");
		new Expectations() {{
			userPasswordEncriptinService.encrypt(userId, "pass");
				result = "encryptedPass";
			userRepository.findByAuthCredential(userId, "encryptedPass");
				result = new UserFixture().get();
			authRepository.newAuthId();
				result = authId;
			authRepository.save((Auth)any);
		}};
		Auth auth = target.authenticate(userId, "pass");
		
		assertThat(auth.id(), is(authId));
	}

}
