package de.tssd.sample.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class SomethingClient {

    private final WebClient webClient;

    // Calls the /api/something endpoint without parameters just to demonstrate a simple GET request
    public String getSomething() {
        return webClient.get()
                .uri("/api/something")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Calls the /api/something endpoint with a query parameter to demonstrate how to pass parameters
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
