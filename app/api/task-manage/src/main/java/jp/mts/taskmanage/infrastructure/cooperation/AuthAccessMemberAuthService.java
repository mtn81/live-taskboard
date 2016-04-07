package jp.mts.taskmanage.infrastructure.cooperation;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import jp.mts.base.util.MapUtils;
import jp.mts.libs.cache.RedisCacheMap;
import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import jp.mts.taskmanage.domain.model.auth.MemberAuthBuilder;
import jp.mts.taskmanage.domain.model.auth.MemberAuthService;
import jp.mts.taskmanage.domain.model.member.MemberId;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambdaworks.redis.RedisClient;

@Service
public class AuthAccessMemberAuthService implements MemberAuthService {

	private AuthAccessConverter authAccessConverter = new AuthAccessConverter();
	private RedisCacheMap<String, MemberAuth> cache;

	@Autowired
	private AuthApi authApi;
	@Autowired
	private RedisClient redisClient;
	
	@PostConstruct
	public void initialize() {
		cache = RedisCacheMap.simpleKeyCache(
				redisClient, "auth", new MemberAuthValueEncoder());
	}
	
	@Override
	public Optional<MemberAuth> establishAuth(String authId) {
		Optional<MemberAuth> cached = cache.get(authId);
		if (cached.isPresent()) {

			MemberAuth memberAuth = cached.get();
			if(memberAuth.isExpired()) {
				authApi.removeAuth(authId);
				return Optional.of(memberAuth);
			}

			MemberAuth expireExtendedAuth = memberAuth.expireExtended();
			cache.put(authId, expireExtendedAuth);
			return Optional.of(expireExtendedAuth);
		} else {

			JSONObject response = authApi.loadAuth(authId);
			if (response == null) return Optional.empty();

			MemberAuth memberAuth = authAccessConverter.toMemberAuth(response);
			cache.put(authId, memberAuth);
			return Optional.of(memberAuth);
		}
	}
	
	static class MemberAuthValueEncoder implements RedisCacheMap.Encoder<Map<String, String>, MemberAuth> {
		@Override
		public Map<String, String> encode(MemberAuth value) {
			return MapUtils.pairs(
					"memberId", value.memberId().value(),
					"expireTime", String.valueOf(value.expireTime().getTime()));
		}
		@Override
		public MemberAuth decode(Map<String, String> value) {
			return new MemberAuthBuilder(
					new MemberAuth(new MemberId(value.get("memberId"))))
				.setExpireTime(new Date(Long.parseLong(value.get("expireTime"))))
				.get();
		}
	}

}
