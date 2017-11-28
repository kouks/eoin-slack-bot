package io.pavelkoch.eoin.modules.test;

import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.Controller;
import io.pavelkoch.eoin.rtm.EventType;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.messaging.Response;

public class TestModule extends Module {

    @Controller(event = EventType.MESSAGE)
    public void test(Message event, Response response) {
        response.channel(event.channel())
                .text("test")
                .send();
    }
}
