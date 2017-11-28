package io.pavelkoch.eoin.modules.todo;

import io.pavelkoch.eoin.http.Request;
import io.pavelkoch.eoin.http.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

class Todo {
    public final String user;
    public final String channel;
    public final String content;
    public final int remindAt;

    private Todo(String user, String channel, String content, int remindAt) {
        this.user = user;
        this.channel = channel;
        this.content = content;
        this.remindAt = remindAt;
    }

    static boolean create(String user, String channel, String content, String remindAt) {
        try {
            Response response = new Request("http://api.dev:8000/todos").post(new HashMap<String, String>() {{
                put("user", user);
                put("channel", channel);
                put("content", content);
                put("remind_at", remindAt);
            }});

            JSONObject json = response.getJson();
            Todo todo = new Todo(json.getString("user"), json.getString("channel"), json.getString("content"), json.getInt("remind_at"));
            TodoReminder.addTodo(todo);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static ArrayList<Todo> all() {
        try {
            ArrayList<Todo> todos = new ArrayList<>();
            Response response = new Request("http://api.dev:8000/todos").get();

            JSONArray jsonTodos = new JSONArray(response.get());

            for (int i = 0; i < jsonTodos.length(); i++) {
                JSONObject todo = jsonTodos.getJSONObject(i);

                todos.add(new Todo(todo.getString("user"), todo.getString("channel"), todo.getString("content"), todo.getInt("remind_at")));
            }

            return todos;
        } catch (Exception e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}
