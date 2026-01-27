package org.sopt.certi_server.global.jwt.core.apple;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apple")
public record AppleOAuthProperties(
	String teamId,
	String clientId,
	String appId,
	String keyId,
	String privateKey,
	String redirectUri
) {}
