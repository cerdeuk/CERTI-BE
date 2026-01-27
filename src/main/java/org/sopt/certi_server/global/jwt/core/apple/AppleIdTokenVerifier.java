package org.sopt.certi_server.global.jwt.core.apple;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.sopt.certi_server.domain.user.dto.response.apple.AppleJwksResponse;
import org.sopt.certi_server.global.client.apple.AppleOAuthFeignClient;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleIdTokenVerifier {

	private final AppleOAuthFeignClient appleOAuthFeignClient;
	private final AppleOAuthProperties props;


	public AppleIdTokenClaims verify(String idToken) {
		try {
			SignedJWT jwt = SignedJWT.parse(idToken);

			String kid = jwt.getHeader().getKeyID();
			JWSAlgorithm alg = jwt.getHeader().getAlgorithm();
			if (!JWSAlgorithm.RS256.equals(alg))
				throw new IllegalStateException("Apple id_token alg must be RS256");

			AppleJwksResponse jwks = appleOAuthFeignClient.keys();
			AppleJwksResponse.AppleJwk jwk = jwks.keys().stream()
				.filter(k -> k.kid().equals(kid))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No matching Apple JWK for kid=" + kid));

			RSAPublicKey publicKey = toRsaPublicKey(jwk.n(), jwk.e());

			if (!jwt.verify(new RSASSAVerifier(publicKey))) {
				throw new IllegalStateException("Invalid Apple id_token signature");
			}

			var claims = jwt.getJWTClaimsSet();

			if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
				throw new IllegalStateException("Invalid issuer");
			}


			Date exp = claims.getExpirationTime();
			if (exp == null || exp.toInstant().isBefore(Instant.now())) {
				throw new IllegalStateException("Token expired");
			}

			String sub = claims.getSubject();
			String email = (String)claims.getClaim("email");
			Boolean emailVerified = (Boolean)claims.getClaim("email_verified");

			return new AppleIdTokenClaims(sub, email, emailVerified);

		} catch (Exception e) {
			throw new IllegalStateException("Failed to verify Apple id_token", e);
		}
	}

	private RSAPublicKey toRsaPublicKey(String n, String e) throws Exception {
		byte[] nBytes = Base64.getUrlDecoder().decode(n);
		byte[] eBytes = Base64.getUrlDecoder().decode(e);

		var modulus = new java.math.BigInteger(1, nBytes);
		var exponent = new java.math.BigInteger(1, eBytes);

		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pk = kf.generatePublic(new RSAPublicKeySpec(modulus, exponent));
		return (RSAPublicKey) pk;
	}

	public record AppleIdTokenClaims(
		String subject,
		String email,
		Boolean emailVerified
	) {}
}
