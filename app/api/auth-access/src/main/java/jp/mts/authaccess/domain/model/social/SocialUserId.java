package jp.mts.authaccess.domain.model.social;

import java.util.List;

import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.base.domain.model.DomainId;

import com.google.common.collect.Lists;

public class SocialUserId extends DomainId<List<Object>> implements UserId {

	private static final long serialVersionUID = 1L;

	public static SocialUserId fromIdValue(String idValue) {
		String[] idValueParts = idValue.split("_", 2);
		return new SocialUserId(UserType.valueOf(idValueParts[0]), idValueParts[1]);
	}

	public SocialUserId(UserType socialUserType, String socialId) {
		super(Lists.<Object>newArrayList(socialUserType, socialId));
	}

	public String socialId() {
		return (String)value().get(1);
	}
	@Override
	public UserType userType() {
		return (UserType)value().get(0);
	}
	@Override
	public String idValue() {
		return userType().name() + "_" + socialId();
	}

}
