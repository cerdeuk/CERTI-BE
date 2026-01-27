package org.sopt.certi_server.global.client.apple;

import org.sopt.certi_server.domain.user.dto.response.apple.AppleJwksResponse;
import org.sopt.certi_server.domain.user.dto.response.apple.AppleTokenResponse;
import org.sopt.certi_server.global.config.AppleConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = "appleOAuthClient",
	url = "https://appleid.apple.com",
	configuration = AppleConfig.class
)
public interface AppleOAuthFeignClient {

	@PostMapping(value = "/auth/token", consumes = "application/x-www-form-urlencoded")
	AppleTokenResponse token(@RequestBody String formUrlEncodedBody);

	@GetMapping("/auth/keys")
	AppleJwksResponse keys();
}
