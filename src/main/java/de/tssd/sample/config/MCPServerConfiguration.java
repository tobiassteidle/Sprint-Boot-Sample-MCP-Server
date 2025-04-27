package de.tssd.sample.config;

import de.tssd.sample.client.SomethingClient;
import de.tssd.sample.tools.SampleTools;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class MCPServerConfiguration {

    // Sample Client for demonstration purposes
    @Bean
    SomethingClient somethingClient(WebClient webClient) {
        return new SomethingClient(webClient);
    }

    // Tool provided via the MCP Server
    @Bean
    SampleTools sampleTools(SomethingClient somethingClient) {
        return new SampleTools(somethingClient);
    }

    // Provides the tools to the MCP Server
    @Bean
    public List<ToolCallback> toolCallbacks(SampleTools sampleTools) {
        return List.of(ToolCallbacks.from(sampleTools));
    }
}
