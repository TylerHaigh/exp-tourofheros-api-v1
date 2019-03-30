package com.github.tylerhaigh.tourofheros.cucumber;

import static java.util.Arrays.asList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class MuleEmbeddedServer extends FunctionalMunitSuite {

    // https://blog.codecentric.de/en/2015/01/mule-esb-testing-part-23-integration-testing-endpoint-mocking-munit/
    private static final List<String> CONFIG_RESOURCES = asList(
        "config.xml",
        "exp-tourofheros-api-v1.xml"
    );


    public MuleEmbeddedServer() {
        super();
    }


    @Override
    protected String getConfigResources() {
        return StringUtils.join(CONFIG_RESOURCES, ',');
    }


    // FunctionalMunitSuite uses protected methods
    // Expose them publically

    public MessageProcessorMocker mockWhenMessageProcessor(String name) {
        return super.whenMessageProcessor(name);
    }

    public MuleMessage mockMuleMessageWithPayload(Object payload) {
        return super.muleMessageWithPayload(payload);
    }

    public MuleEvent mockTestEvent(Object payload) throws Exception {
        return super.testEvent(payload);
    }

    public MuleEvent runMuleFlow(String name, MuleEvent event) throws MuleException {
        return super.runFlow(name, event);
    }

    public void setUpMunit() {
        super.__setUpMunit();
    }

    public void restartMunit() {
        super.__restartMunit();
    }
}
