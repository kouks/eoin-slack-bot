package io.pavelkoch.eoin.modules.todo;

import io.pavelkoch.eoin.rtm.Controller;
import io.pavelkoch.eoin.rtm.Dialogue;
import io.pavelkoch.eoin.rtm.EventType;
import io.pavelkoch.eoin.rtm.conversations.Conversation;
import io.pavelkoch.eoin.rtm.events.Hello;
import io.pavelkoch.eoin.rtm.messaging.Response;
import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.events.Message;

public class TodoModule extends Module {

    @Controller(event = EventType.HELLO)
    public void runTodoReminder(Hello event, Response response) {
        (new Thread(new TodoReminder(response, Todo.all()))).start();
    }

    @Dialogue(next = "addTodoName", pattern = ".*(add|create|new).*(todo|to-do).*")
    public void startAddingTodo(Message event, Response response, Conversation conversation) {
        response.channel(event.channel()).text("What kind?").send();
    }

    @Dialogue(next = "addTodoDueTime")
    public void addTodoName(Message event, Response response, Conversation conversation) {
        response.channel(event.channel())
                .text("OK, done, added todo: *" + event.text() + "*, what time shall I remind you?")
                .send();

    }

    @Dialogue()
    public void addTodoDueTime(Message event, Response response, Conversation conversation) {
        if (! Todo.create(event.user(), event.channel(), conversation.history("addTodoName").text(), event.text())) {
            response.channel(event.channel())
                    .text("Sorry, this is not a valid time format, maybe try again?")
                    .send();

            conversation.repeat("addTodoDueTime");
            return;
        }

        response.channel(event.channel())
                .text("Hey, I added time: *" + event.text() + "*, to the todo: *" + conversation.history("addTodoName").text() + "*")
                .send();
    }
}
