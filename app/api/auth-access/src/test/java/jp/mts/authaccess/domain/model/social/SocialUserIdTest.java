package jp.mts.authaccess.domain.model.social;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.authaccess.domain.model.UserType;

import org.junit.Test;

public class SocialUserIdTest {

	@Test
	public void test_fromIdValue() {
		
		assertThat(SocialUserId.fromIdValue("GOOGLE_u01_a"), 
				is(new SocialUserId(UserType.GOOGLE, "u01_a")));
	}

}
