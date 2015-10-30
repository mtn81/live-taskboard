package jp.mts.taskmanage.infrastructure.cooperation;


import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.auth.MemberAuth;

import org.json.JSONObject;

public class AuthAccessConverter {

	public MemberAuth toMemberAuth(JSONObject json) {
		String memberId = json.getJSONObject("data").getString("userId");
		return new MemberAuth(new MemberId(memberId));
	}
}
