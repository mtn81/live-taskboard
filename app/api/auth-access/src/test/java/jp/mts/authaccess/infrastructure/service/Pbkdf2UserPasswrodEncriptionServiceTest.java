package jp.mts.authaccess.infrastructure.service;

import jp.mts.authaccess.domain.model.proper.ProperUserId;

import org.junit.Test;

public class Pbkdf2UserPasswrodEncriptionServiceTest {

	@Test
	public void test() {
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new ProperUserId("taro"), "pass")
		);
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new ProperUserId("taro1"), "pass")
		);
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new ProperUserId("taro2"), "pass")
		);
	}

}
