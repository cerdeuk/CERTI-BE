package org.sopt.certi_server.global.client.discord;

import org.sopt.certi_server.global.discord.dto.DiscordWebhookEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = "discordwebhookClient",
	url = "${discord.webhook-url}"
)
public interface DiscordWebhookClient {
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	void send(@RequestBody DiscordWebhookEvent message);
}
