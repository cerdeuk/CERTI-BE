package org.sopt.certi_server.global.jwt.core.apple;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public final class AppleKeyParser {

	private AppleKeyParser() {}

	public static ECPrivateKey parseECPrivateKey(String privateKeyPem) {
		try {
			String normalized = privateKeyPem
				.replace("\\n", "\n")
				.trim();

			// 1) PEM 헤더/푸터 포함된 경우
			if (normalized.contains("BEGIN PRIVATE KEY")) {
				try (PemReader pemReader = new PemReader(new StringReader(normalized))) {
					PemObject pemObject = pemReader.readPemObject();
					byte[] pkcs8 = pemObject.getContent();
					KeyFactory kf = KeyFactory.getInstance("EC");
					return (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
				}
			}

			// 2) 그냥 base64만 들어온 경우
			byte[] pkcs8 = Base64.getDecoder().decode(normalized);
			KeyFactory kf = KeyFactory.getInstance("EC");
			return (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8));

		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid apple private key (p8)", e);
		}
	}
}
