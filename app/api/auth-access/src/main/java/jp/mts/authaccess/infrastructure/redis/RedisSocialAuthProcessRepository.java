package jp.mts.authaccess.infrastructure.redis;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessBuilder;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessRepository;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.base.util.MapUtils;
import jp.mts.libs.cache.RedisCacheMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lambdaworks.redis.RedisClient;

@Repository
public class RedisSocialAuthProcessRepository implements SocialAuthProcessRepository {
	
	@Autowired
	private RedisClient redisClient;
	
	private RedisCacheMap<SocialAuthProcessId, SocialAuthProcess> redisCacheMap;
	
	@PostConstruct
	public void initialize() {
		redisCacheMap = new RedisCacheMap<SocialAuthProcessId, SocialAuthProcess>(
				redisClient, "social_auth_process",
				new SocialAuthProcessIdKeyEncoder(),
				new SocialAuthProcessValueEncoder());
	}
	
	@Override
	public SocialAuthProcessId newAuthProcessId() {
		return new SocialAuthProcessId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}

	@Override
	public void save(SocialAuthProcess socialAuthProcess) {
		redisCacheMap.put(socialAuthProcess.id(), socialAuthProcess);
	}

	@Override
	public Optional<SocialAuthProcess> findById(SocialAuthProcessId authProcessId) {
		return redisCacheMap.get(authProcessId);
	}

	@Override
	public void remove(SocialAuthProcess socialAuthProcess) {
		redisCacheMap.remove(socialAuthProcess.id());
	}
	
	static class SocialAuthProcessIdKeyEncoder 
		implements RedisCacheMap.Encoder<String, SocialAuthProcessId>{
		@Override
		public String encode(SocialAuthProcessId id) {
			return id.value();
		}
		@Override
		public SocialAuthProcessId decode(String idStr) {
			return new SocialAuthProcessId(idStr);
		}
	}
	static class SocialAuthProcessValueEncoder 
		implements RedisCacheMap.Encoder<Map<String, String>, SocialAuthProcess> {

		@Override
		public Map<String, String> encode(SocialAuthProcess process) {
			return MapUtils.pairs(
				"authProcessId", process.id().value(),
				"stateToken", process.stateToken(),
				"authLocation", process.authLocation(),
				"acceptClientUrl", process.acceptClientUrl(),
				"rejectClientUrl", process.rejectClientUrl(),
				"firstUse", String.valueOf(process.firstUse()),
				"userType", process.userType().name(),
				"socialUserId", process.socialUserId().idValue());
		}
		@Override
		public SocialAuthProcess decode(Map<String, String> encodedProcess) {
			return new SocialAuthProcessBuilder(
					new SocialAuthProcess(
							new SocialAuthProcessId(encodedProcess.get("authProcessId")), 
							encodedProcess.get("stateTocken"), 
							encodedProcess.get("authLocation"), 
							encodedProcess.get("acceptClientUrl"), 
							encodedProcess.get("rejectClientUrl"), 
							UserType.valueOf(encodedProcess.get("userType"))))
				.setFirstUse(Boolean.valueOf(encodedProcess.get("firstUse")))
				.setSocialUserId(SocialUserId.fromIdValue(encodedProcess.get("socialUserId")))
				.get();
		}
	}
}
