package jp.mts.authaccess.domain.model.social;

import java.util.List;

import jp.mts.authaccess.domain.model.UserId;
import jp.mts.base.domain.model.DomainId;

import com.google.common.collect.Lists;

public class SocialUserId extends DomainId<List> implements UserId {

	public SocialUserId(SocialUserType socialUserType, String socialId) {
		super(Lists.newArrayList(socialUserType, socialId));
	}

	public SocialUserType socialUserType() {
		return (SocialUserType)value().get(0);
	}
	public String socialId() {
		return (String)value().get(1);
	}
	@Override
	public String idValue() {
		return socialUserType().name() + "_" + socialId();
	}
}
