package org.sopt.certi_server;

import org.sopt.certi_server.global.config.AppleConfig;
import org.sopt.certi_server.global.jwt.core.apple.AppleOAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.sopt.certi_server.global.client")
@EnableJpaAuditing
@EnableConfigurationProperties(AppleOAuthProperties.class)
public class CertiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertiServerApplication.class, args);
    }

}
