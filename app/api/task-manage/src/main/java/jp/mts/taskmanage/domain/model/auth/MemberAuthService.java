package jp.mts.taskmanage.domain.model.auth;

import java.util.Optional;

public interface MemberAuthService {
	Optional<MemberAuth> establishAuth(String authId);
}
