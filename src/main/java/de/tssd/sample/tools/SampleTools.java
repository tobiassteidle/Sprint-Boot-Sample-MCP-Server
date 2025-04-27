package de.tssd.sample.tools;

import de.tssd.sample.client.SomethingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SampleTools {

    private final SomethingClient somethingClient;

    @Tool(name = "sample_do_something", description = "This is a sample tool that does something")
    public String doSomething() {
        return somethingClient.getSomething();
    }

    @Tool(name = "sample_do_something_with_param", description = "This is a sample tool that does something with a parameter")
    public String doSomethingWithParam(String param) {
        return somethingClient.getSomethingWithParam(param);
    }
}
