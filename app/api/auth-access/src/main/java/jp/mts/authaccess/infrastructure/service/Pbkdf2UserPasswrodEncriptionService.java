package jp.mts.authaccess.infrastructure.service;

import java.security.MessageDigest;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.PasswordEncriptionService;

/**
 * ref: http://www.casleyconsulting.co.jp/blog-engineer/java/%E3%80%90java-se-8%E9%99%90%E5%AE%9A%E3%80%91%E5%AE%89%E5%85%A8%E3%81%AA%E3%83%91%E3%82%B9%E3%83%AF%E3%83%BC%E3%83%89%E3%82%92%E7%94%9F%E6%88%90%E3%81%99%E3%82%8B%E6%96%B9%E6%B3%95/ 
 */
@Service
public class Pbkdf2UserPasswrodEncriptionService implements PasswordEncriptionService {

	@Override
	public String encrypt(UserId userid, String plainPassword) {
		try{
			PBEKeySpec keySpec = new PBEKeySpec(
					plainPassword.toCharArray(), 
					sha256Value(userid.value()), 1000, 256);
			SecretKey secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpec);
			StringBuilder sb = new StringBuilder();
			for(byte b : secretKey.getEncoded()){
				sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private byte[] sha256Value(String s){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(s.getBytes());
			return digest.digest();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
