package io.pavelkoch.eoin.modules.todo;

import io.pavelkoch.eoin.rtm.Dialogue;
import io.pavelkoch.eoin.rtm.conversations.Conversation;
import io.pavelkoch.eoin.rtm.messaging.Response;
import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.events.Message;

public class TodoModule extends Module {

    @Dialogue(next = "addTodoName", pattern = ".*(add|create|new).*(todo|to-do).*")
    public void startAddingTodo(Message event, Response response, Conversation conversation) {
        response.channel(event.channel()).text("What kind?").send();
    }

    @Dialogue(next = "addTodoDueTime")
    public void addTodoName(Message event, Response response, Conversation conversation) {
        response.channel(event.channel())
                .text("ok, done, added todo: *" + event.text() + "*, what time should I remind you?")
                .send();

    }

    @Dialogue()
    public void addTodoDueTime(Message event, Response response, Conversation conversation) {
        if (event.text().equals("asd")) {
            response.channel(event.channel())
                    .text("sorry, this is not a valid time format, maybe try again?")
                    .send();

            conversation.repeat("addTodoDueTime");
            return;
        }

        response.channel(event.channel())
                .text("hey, I added time: *" + event.text() + "*, to the todo: *" + conversation.history("addTodoName") + "*")
                .send();
    }
}
