package de.tssd.sample.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class SomethingClient {

    private final WebClient webClient;

    public String getSomething() {
        return webClient.get()
                .uri("/api/something")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getSomethingWithParam(String param) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/something")
                        .queryParam("param", param)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
