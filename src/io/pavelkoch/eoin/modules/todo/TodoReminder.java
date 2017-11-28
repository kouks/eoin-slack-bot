package io.pavelkoch.eoin.modules.todo;

import io.pavelkoch.eoin.rtm.messaging.Response;

import java.util.ArrayList;
import java.util.List;

public class TodoReminder implements Runnable {
    private static List<Todo> todos = new ArrayList<>();
    private final Response response;

    static void addTodo(Todo todo) {
        todos.add(todo);
    }

    TodoReminder(Response response, ArrayList<Todo> todos) {
        this.response = response;
        TodoReminder.todos = todos;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < todos.size(); i++) {
                Todo todo = todos.get(i);

                if (todo.remindAt < (int) (System.currentTimeMillis() / 1000)) {
                    this.response
                            .channel(todo.channel)
                            .text("Hey, <@" + todo.user + ">, I am reminding you about the todo: " + todo.content)
                            .send();

                    todos.remove(i);
                }
            }

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
