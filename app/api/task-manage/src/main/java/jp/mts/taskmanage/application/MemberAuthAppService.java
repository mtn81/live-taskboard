package jp.mts.taskmanage.application;

import java.util.Optional;

import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import jp.mts.taskmanage.domain.model.auth.MemberAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthAppService {
	
	@Autowired
	private MemberAuthService memberAuthService;

	public boolean validateAuth(String authId) {
		Optional<MemberAuth> memberAuthResult = memberAuthService.establishAuth(authId);
		if (!memberAuthResult.isPresent()) return false;
		
		MemberAuth memberAuth = memberAuthResult.get();
		return !memberAuth.isExpired();
	}
}
