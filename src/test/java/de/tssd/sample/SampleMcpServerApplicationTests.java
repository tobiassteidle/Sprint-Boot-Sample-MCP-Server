package de.tssd.sample;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.banner-mode=log", // to enable logging
                "spring.main.web-application-type=reactive",  // to use WebFluxSseClientTransport in McpClient
                "spring.ai.mcp.server.stdio=false" // to disable stdio server
        })
class SampleMcpServerApplicationTests {

    @DisplayName("Test if the application context loads successfully, tools are registered, and the server is running.")
    @Test
    void selectToolList() {
        withClient(client -> {
            final var toolsList = client.listTools();
            assertThat(toolsList).isNotNull();
            assertThat(toolsList.tools()).hasSize(2);
        });
    }

    @DisplayName("Test if the tool 'sample_do_something' works correctly.")
    @Test
    void tool_doSomething() throws IOException {
        enqueueMockResponse(200, "{\"projects\": [{\"key\": \"projectKey\", \"name\": \"Project Name\"}]}");
        withClient(client -> {
            final var request = new McpSchema.CallToolRequest("sample_do_something", Map.of("operation", "string"));
            final var response = client.callTool(request);
            assertThat(response).isNotNull();
            assertThat(response.isError()).isFalse();
        });
    }

    @DisplayName("Test if the tool 'sample_do_something_with_param' with an error.")
    @Test
    void tool_doSomethingWithParam() throws IOException {
        enqueueMockResponse(500, "{\"error\": \"Internal Server Error\"}");

        withClient(client -> {
            final var request = new McpSchema.CallToolRequest("sample_do_something_with_param", Map.of("operation", "string", "param", "something"));
            final var response = client.callTool(request);
            assertThat(response).isNotNull();
            assertThat(response.isError()).isTrue();
        });
    }

    // MockWebServer configuration to simulate the target service
    static final int TARGET_SERVICE_PORT = TestSocketUtils.findAvailableTcpPort();

    // Provides a WebClient bean for the test
    @TestBean
    private WebClient webClient;

    static WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:" + TARGET_SERVICE_PORT)
                .build();
    }

    // MockWebServer instance to simulate the target service
    private static MockWebServer mockWebServer;

    // Start the MockWebServer before each test
    @BeforeEach
    void setupMockWebServer() throws IOException {
        if (mockWebServer == null) {
            mockWebServer = new MockWebServer();
            mockWebServer.start(TARGET_SERVICE_PORT);
        }
    }

    // Enqueue a mock response for the MockWebServer
    private void enqueueMockResponse(final int statusCode, final String responseBody) throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(statusCode)
                .setBody(responseBody));
    }

    // Port for the MCP server
    @LocalServerPort
    private int port;

    // Helper method to create a McpClient and execute a function with it
    private void withClient(Consumer<McpSyncClient> func) {
        final var webclient = WebClient.builder().baseUrl("http://localhost:" + port);
        final var transport = WebFluxSseClientTransport.builder(webclient).build();
        final var mcpClient = McpClient.sync(transport).build();
        mcpClient.initialize();
        mcpClient.ping();
        func.accept(mcpClient);
        mcpClient.closeGracefully();
    }
}
