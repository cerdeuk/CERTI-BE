package org.sopt.certi_server.global.discord.service;

import org.sopt.certi_server.global.client.discord.DiscordWebhookClient;
import org.sopt.certi_server.global.discord.dto.DiscordWebhookEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordService {

	private final DiscordWebhookClient discordWebhookClient;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendDiscordWebhookMessage(DiscordWebhookEvent message) {
		try{
			discordWebhookClient.send(message);
		} catch (Exception e){
			log.error(e.getMessage());
		}
	}
}
