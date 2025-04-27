package de.tssd.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpClientConfigration {

    // Note: In a real-world scenario, you would typically configure the base URL and other settings
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
    }
}
