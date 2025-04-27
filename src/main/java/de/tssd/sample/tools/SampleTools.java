package de.tssd.sample.tools;

import de.tssd.sample.client.SomethingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;

@RequiredArgsConstructor
public class SampleTools {

    private final SomethingClient somethingClient;

    // Sample tool methods for demonstration purposes
    @Tool(name = "sample_do_something", description = "This is a sample tool that does something")
    public String doSomething() {
        return somethingClient.getSomething();
    }

    // Sample tool method that takes a parameter
    @Tool(name = "sample_do_something_with_param", description = "This is a sample tool that does something with a parameter")
    public String doSomethingWithParam(String param) {
        return somethingClient.getSomethingWithParam(param);
    }
}
