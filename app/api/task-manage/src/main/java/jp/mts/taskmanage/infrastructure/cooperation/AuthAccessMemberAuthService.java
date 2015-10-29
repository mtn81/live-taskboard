package jp.mts.taskmanage.infrastructure.cooperation;

import java.util.Optional;

import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import jp.mts.taskmanage.domain.model.auth.MemberAuthService;

public class AuthAccessMemberAuthService implements MemberAuthService {

	@Override
	public Optional<MemberAuth> establishAuth(String authId) {
		return null;
	}

}
