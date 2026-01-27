package org.sopt.certi_server.global.jwt.core.apple;

import java.security.interfaces.ECPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class AppleClientSecretProvider {

	@Value("${apple.team-id}")
	private String teamId;

	@Value("${apple.key-id}")
	private String keyId;

	@Value("${apple.app-id}")
	private String appId;

	@Value("${apple.private-key}")
	private String privateKeyPem;

	public String createClientSecret() {
		Instant now = Instant.now();
		Instant exp = now.plus(Duration.ofMinutes(10));

		try {
			ECPrivateKey ecPrivateKey = AppleKeyParser.parseECPrivateKey(privateKeyPem);
			JWSSigner signer = new ECDSASigner(ecPrivateKey);

			JWTClaimsSet claims = new JWTClaimsSet.Builder()
				.issuer(teamId)
				.issueTime(Date.from(now))
				.expirationTime(Date.from(exp))
				.audience("https://appleid.apple.com")
				.subject(appId) // ✅ appId가 들어와야 함
				.build();

			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
				.keyID(keyId)
				.type(JOSEObjectType.JWT)
				.build();

			SignedJWT jwt = new SignedJWT(header, claims);
			jwt.sign(signer);
			return jwt.serialize();

		} catch (Exception e) {
			throw new IllegalStateException("Failed to create apple client_secret", e);
		}
	}
}
