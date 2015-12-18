package jp.mts.authaccess.infrastructure.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessRepository;

import org.springframework.stereotype.Repository;

@Repository
public class RedisSocialAuthProcessRepository implements SocialAuthProcessRepository {
	
	private Map<SocialAuthProcessId, SocialAuthProcess> map = new HashMap<>();

	@Override
	public SocialAuthProcessId newAuthProcessId() {
		return new SocialAuthProcessId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}

	@Override
	public void save(SocialAuthProcess socialAuthProcess) {
		map.put(socialAuthProcess.id(), socialAuthProcess);
	}

	@Override
	public Optional<SocialAuthProcess> findById(SocialAuthProcessId authProcessId) {
		return map.containsKey(authProcessId) ? 
				Optional.of(map.get(authProcessId)) : Optional.empty();
	}

	@Override
	public void remove(SocialAuthProcess socialAuthProcess) {
		map.remove(socialAuthProcess.id());
	}
}
