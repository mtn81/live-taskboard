package jp.mts.authaccess.domain.model.proper;

import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.base.domain.model.DomainId;

public class ProperUserId extends DomainId<String> implements UserId {

	private static final long serialVersionUID = 1L;

	public ProperUserId(String value){
		super(value);
	}

	@Override
	public String idValue() {
		return value();
	}

	@Override
	public UserType userType() {
		return UserType.PROPER;
	}
}
