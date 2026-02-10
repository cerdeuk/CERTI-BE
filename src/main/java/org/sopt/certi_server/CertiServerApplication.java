package org.sopt.certi_server;

import org.sopt.certi_server.global.config.AppleConfig;
import org.sopt.certi_server.global.jwt.core.apple.AppleOAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.sopt.certi_server.global.client")
@EnableJpaAuditing
@EnableConfigurationProperties(AppleOAuthProperties.class)
@EnableAsync
public class CertiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertiServerApplication.class, args);
    }

}
