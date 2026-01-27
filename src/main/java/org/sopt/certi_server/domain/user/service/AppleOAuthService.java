package org.sopt.certi_server.domain.user.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.dto.response.apple.AppleTokenResponse;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.global.client.apple.AppleOAuthFeignClient;
import org.sopt.certi_server.global.jwt.core.apple.AppleClientSecretProvider;
import org.sopt.certi_server.global.jwt.core.apple.AppleIdTokenVerifier;
import org.sopt.certi_server.global.jwt.core.apple.AppleOAuthProperties;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleOAuthService implements SocialService {

	private final AppleOAuthFeignClient appleOAuthFeignClient;
	private final AppleClientSecretProvider clientSecretProvider;
	private final AppleIdTokenVerifier idTokenVerifier;
	private final AppleOAuthProperties props;

	@Override
	public LoginUriResponse getAuthorizationUri() {
		throw new UnsupportedOperationException("App flow does not use server-side authorization uri.");
	}

	@Override
	public OAuthUserInformation getUserInfo(String code) {
		String clientId = props.appId();
		String clientSecret = clientSecretProvider.createClientSecret();

		String body = form(
			"grant_type", "authorization_code",
			"code", code,
			"client_id", clientId,
			"client_secret", clientSecret,
			"redirect_uri", props.redirectUri()
		);

		AppleTokenResponse token = appleOAuthFeignClient.token(body);

		var claims = idTokenVerifier.verify(token.idToken());

		return new OAuthUserInformation(
			appleSubToLongIdStable(claims.subject()).toString(),
			SocialType.APPLE,
			claims.email(),
			"APPLE_USER",
			null
		);
	}

	// 앱에서 identityToken(id_token)을 바로 주는 경우
	@Override
	public OAuthUserInformation getUserInfoByAccessToken(String idToken) {
		String clientId = props.appId();
		var claims = idTokenVerifier.verify(idToken);

		return new OAuthUserInformation(
			appleSubToLongIdStable(claims.subject()).toString(),
			SocialType.APPLE,
			claims.email(),
			"APPLE_USER",
			null
		);
	}

	private Long appleSubToLongIdStable(String sub) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(sub.getBytes(StandardCharsets.UTF_8));
			long v = 0L;
			for (int i = 0; i < 8; i++) v = (v << 8) | (digest[i] & 0xffL);
			return v & Long.MAX_VALUE;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private String form(String... kv) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < kv.length; i += 2) {
			if (i > 0) sb.append("&");
			sb.append(encode(kv[i])).append("=").append(encode(kv[i + 1]));
		}
		return sb.toString();
	}

	private String encode(String s) {
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}
}

