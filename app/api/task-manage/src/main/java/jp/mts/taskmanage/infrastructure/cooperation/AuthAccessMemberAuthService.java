package jp.mts.taskmanage.infrastructure.cooperation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import jp.mts.taskmanage.domain.model.auth.MemberAuthService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthAccessMemberAuthService implements MemberAuthService {

	@Autowired
	private AuthApi authApi;
	private AuthAccessConverter authAccessConverter = new AuthAccessConverter();
	private Map<String, MemberAuth> cache = new HashMap<>();
	
	@Override
	public Optional<MemberAuth> establishAuth(String authId) {
		if (cache.containsKey(authId)) {
			MemberAuth memberAuth = cache.get(authId);
			if(memberAuth.isExpired()) return Optional.of(memberAuth);
			return Optional.of(memberAuth.expireExtended());
		}

		JSONObject response = authApi.loadAuth(authId);
		if (response == null) return Optional.empty();

		MemberAuth memberAuth = authAccessConverter.toMemberAuth(response);
		cache.put(authId, memberAuth);
		return Optional.of(memberAuth);
	}

}
