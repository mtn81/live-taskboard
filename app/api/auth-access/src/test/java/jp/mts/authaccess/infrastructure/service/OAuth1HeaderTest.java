package jp.mts.authaccess.infrastructure.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.util.Map;

import jp.mts.authaccess.infrastructure.service.OAuth1Header.OAuth1SignatureBuilder;
import jp.mts.base.util.MapUtils;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class OAuth1HeaderTest {
	
	public static class AuthenticationHeaderTest {
		
	    @Mocked OAuth1SignatureBuilder oAuth1SignatureBuilder;
	    @Mocked RandomStringUtils RandomStringUtils;
	    @Mocked Instant instant;

	    @Test
	    public void test_buildAuthenticationHeader() {
	    	new Expectations() {{
	    		RandomStringUtils.randomAlphanumeric(32); 
	    			result="xxx";
	    		Instant.now();
	    			result = instant;
	    		instant.getEpochSecond();
	    			result = 100;
	    		oAuth1SignatureBuilder.build((Map<String, String>)any);
	    			result = "sig01";
	    	}};
	    	
	    	
	    	OAuth1Header target = new OAuth1Header("appkey01", "access01", "http://callback", oAuth1SignatureBuilder);
	    	
	    	assertThat(target.buildAuthenticationHeader(), is(
	    		"OAuth "
	    		+ "oauth_callback=\"http%3A%2F%2Fcallback\","
	    		+ " oauth_consumer_key=\"appkey01\","
	    		+ " oauth_nonce=\"xxx\","
	    		+ " oauth_signature_method=\"HMAC-SHA1\","
	    		+ " oauth_timestamp=\"100\","
	    		+ " oauth_version=\"1.0\","
	    		+ " oauth_token=\"access01\","
	    		+ " oauth_signature=\"sig01\""
	    	));
	    }
	}

	public static class SignatureTest {
		
		@Test
		public void test_signingKey() {
			
			OAuth1SignatureBuilder target 
				= new OAuth1SignatureBuilder("POST", "http://test", "secret01");
			assertThat(target.buildSigningKey(), is("secret01&"));
		}
		@Test
		public void test_signatureBaseString() {
			//see: https://dev.twitter.com/oauth/overview/creating-signatures

			OAuth1SignatureBuilder target 
				= new OAuth1SignatureBuilder("POST", "https://api.twitter.com/1/statuses/update.json", "secret01");
			
			String actual = target.buildSignatureBaseString(MapUtils.pairs(
					"status","Hello Ladies + Gentlemen, a signed OAuth request!", 
					"include_entities", "true",
					"oauth_consumer_key", "xvz1evFS4wEEPTGEFPHBog",
					"oauth_nonce", "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
					"oauth_signature_method", "HMAC-SHA1",
					"oauth_timestamp", "1318622958",
					"oauth_token", "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
					"oauth_version", "1.0"));
			
			
			assertThat(actual, is(
					"POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"));
			
		}
		@Test
		public void test_toalSegnature() {
			//see: https://dev.twitter.com/oauth/overview/creating-signatures
			
			OAuth1SignatureBuilder target 
				= new OAuth1SignatureBuilder(
						"POST", 
						"https://api.twitter.com/1/statuses/update.json", 
						"kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw",
						"LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE");
			
			String actual = target.build(MapUtils.pairs(
					"status","Hello Ladies + Gentlemen, a signed OAuth request!", 
					"include_entities", "true",
					"oauth_consumer_key", "xvz1evFS4wEEPTGEFPHBog",
					"oauth_nonce", "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
					"oauth_signature_method", "HMAC-SHA1",
					"oauth_timestamp", "1318622958",
					"oauth_token", "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
					"oauth_version", "1.0"));
			
			
			assertThat(actual, is(
					"tnnArxj06cWHq44gCs1OSKk/jLY="));
			
		}
	}
}
