package org.sopt.certi_server.domain.user.controller;

import org.sopt.certi_server.global.jwt.core.apple.AppleIdTokenVerifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/apple")
public class AppleTestController {

	private final AppleIdTokenVerifier verifier;

	@PostMapping("/verify")
	public AppleIdTokenVerifier.AppleIdTokenClaims verify(@RequestBody VerifyReq req) {
		return verifier.verify(req.idToken);
	}

	public record VerifyReq(
		String idToken
	) {
	}
}
