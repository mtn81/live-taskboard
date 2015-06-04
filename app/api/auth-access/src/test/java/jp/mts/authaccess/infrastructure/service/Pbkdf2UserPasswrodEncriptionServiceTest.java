package jp.mts.authaccess.infrastructure.service;

import jp.mts.authaccess.domain.model.UserId;

import org.junit.Test;

public class Pbkdf2UserPasswrodEncriptionServiceTest {

	@Test
	public void test() {
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new UserId("taro"), "pass")
		);
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new UserId("taro1"), "pass")
		);
		System.out.println(
			new Pbkdf2UserPasswrodEncriptionService()
				.encrypt(new UserId("taro2"), "pass")
		);
	}

}
