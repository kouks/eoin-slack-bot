package io.pavelkoch.eoin.rtm.conversations;

import java.util.HashMap;
import java.util.Map;

public class ConversationStore implements Conversation {
    private static Map<String, ConversationStore> openConversations = new HashMap<>();
    private final String user;
    private String nextMethod;
    private Map<String, String> history = new HashMap<>();

    private ConversationStore(String user, String nextMethod) {
        this.user = user;
        this.nextMethod = nextMethod;
    }

    public void stop() {
        openConversations.remove(this.user);
    }

    public void skip(String stage) {
        this.setNextMethod(stage);
    }

    public static boolean exists(String user) {
        return openConversations.containsKey(user);
    }

    public static void create(String user, String nextMethod) {
        openConversations.put(user, new ConversationStore(user, nextMethod));
    }

    public static ConversationStore find(String user) {
        return openConversations.get(user);
    }

    public void pushMessageToHistory(String stage, String message) {
        this.history.put(stage, message);
    }

    public String history(String stage) {
        return this.history.get(stage);
    }

    public String getNextMethod() {
        return this.nextMethod;
    }

    public void setNextMethod(String nextMethod) {
        this.nextMethod = nextMethod;
    }
}
