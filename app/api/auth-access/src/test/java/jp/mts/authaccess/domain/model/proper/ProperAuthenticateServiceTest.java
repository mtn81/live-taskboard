package jp.mts.authaccess.domain.model.proper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.proper.PasswordEncriptionService;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.base.domain.model.DomainEventPublisher;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class ProperAuthenticateServiceTest {

	@Mocked	ProperUserRepository userRepository;
	@Mocked	AuthRepository authRepository;
	@Mocked PasswordEncriptionService userPasswordEncriptinService;
	@Mocked DomainEventPublisher domainEventPublisher;
	
	@Test
	public void test() {
		ProperAuthenticateService target = new ProperAuthenticateService(
				userRepository, authRepository, userPasswordEncriptinService, domainEventPublisher);

		final ProperUserId userId = new ProperUserId("u01");
		final AuthId authId = new AuthId("a01");
		new Expectations() {{
			userPasswordEncriptinService.encrypt(userId, "pass");
				result = "encryptedPass";
			userRepository.findByAuthCredential(userId, "encryptedPass");
				result = new ProperUserFixture().get();
			authRepository.newAuthId();
				result = authId;
			authRepository.save((Auth)any);
		}};
		Auth auth = target.authenticate(userId, "pass");
		
		assertThat(auth.id(), is(authId));
	}

}
