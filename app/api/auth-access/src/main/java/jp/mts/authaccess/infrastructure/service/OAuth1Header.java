package jp.mts.authaccess.infrastructure.service;

import static jp.mts.base.util.Assertions.assertNonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jp.mts.base.util.Assertions;
import jp.mts.base.util.MapUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

public class OAuth1Header {
	private String appKey;
	private String oauthToken;
	private String callbackUrl;
	private OAuth1SignatureBuilder oAuth1SignatureBuilder;
	
	public OAuth1Header(
			String appKey, 
			String accessToken, 
			String callbackUrl,
			OAuth1SignatureBuilder othAuth1SignatureBuilder) {
		
		Assertions.assertNonNull(appKey);
		Assertions.assertNonNull(callbackUrl);
		Assertions.assertNonNull(othAuth1SignatureBuilder);
		
		this.appKey = appKey;
		this.oauthToken = accessToken;
		this.callbackUrl = callbackUrl;
		this.oAuth1SignatureBuilder = othAuth1SignatureBuilder;
	}

	public String buildAuthenticationHeader(Map<String, String> paramMap) {
		
		Map<String, String> oAuthParams = MapUtils.pairs(
			"oauth_callback", callbackUrl,
			"oauth_consumer_key", appKey,
			"oauth_nonce", RandomStringUtils.randomAlphanumeric(32),
			"oauth_signature_method", "HMAC-SHA1",
			"oauth_timestamp", String.valueOf(Instant.now().getEpochSecond()),
			"oauth_version", "1.0");
		if (oauthToken != null) {
			oAuthParams.put("oauth_token", oauthToken);
		}
		
		oAuthParams.put("oauth_signature", oAuth1SignatureBuilder.build(
				MapUtils.sum(oAuthParams, paramMap)));
		
		StringBuilder result = new StringBuilder(); 
		
		result.append("OAuth ");
		oAuthParams.forEach((key, value) -> {
			result.append(key);
			result.append("=\"");
			result.append(percentEncode(value));
			result.append("\", ");
		});
		
		return result.replace(result.length() - 2, result.length(), "").toString();
	}
	
	
    private static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }
    
    
	public static class OAuth1SignatureBuilder {
	
		private String httpMethod;
		private String baseUrl;
		private String appSecret;
		private String tokenSecret;
		
		public OAuth1SignatureBuilder(
				String httpMethod, 
				String baseUrl,
				String appSecret) {
	
			this(httpMethod, baseUrl, appSecret, "");
		}
		public OAuth1SignatureBuilder(
				String httpMethod, 
				String baseUrl,
				String appSecret, 
				String tokenSecret) {
			
			assertNonNull(httpMethod);
			assertNonNull(baseUrl);
			assertNonNull(appSecret);
			assertNonNull(tokenSecret);
			
			this.httpMethod = httpMethod;
			this.baseUrl = baseUrl;
			this.appSecret = appSecret;
			this.tokenSecret = tokenSecret;
		}
		
		public String build(Map<String, String> params) {
			try {
			    SecretKeySpec secretKeySpec = new SecretKeySpec(buildSigningKey().getBytes(), "HmacSHA1");
			    Mac mac = Mac.getInstance("HmacSHA1");
			    mac.init(secretKeySpec);
			    byte[] signature = mac.doFinal(buildSignatureBaseString(params).getBytes());
			    return new String(Base64.encodeBase64(signature));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	
		String buildSignatureBaseString(Map<String, String> paramMap) {
			Assertions.assertNonNull(paramMap);
			
			StringBuilder paramString = new StringBuilder();
			new TreeMap<>(
					MapUtils.map(paramMap, key -> percentEncode(key), value -> percentEncode(value)))
				.forEach((key,value) -> {
					paramString.append(key);
					paramString.append("=");
					paramString.append(value);
					paramString.append("&");
				});
			paramString.replace(paramString.length() - 1, paramString.length(), "");
			
			return new StringBuilder()
				.append(httpMethod)
				.append("&")
				.append(percentEncode(baseUrl))
				.append("&")
				.append(percentEncode(paramString.toString()))
				.toString();
		}

		String buildSigningKey() {
			return new StringBuilder()
				.append(percentEncode(appSecret))
				.append("&")
				.append(percentEncode(tokenSecret))
				.toString();
		}
		
	}
}
