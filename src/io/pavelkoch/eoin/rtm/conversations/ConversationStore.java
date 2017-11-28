package io.pavelkoch.eoin.rtm.conversations;

import java.util.HashMap;
import java.util.Map;

public class ConversationStore implements Conversation {
    /**
     * The open conversation instances mapped with a user ID.
     */
    private static Map<String, ConversationStore> openConversations = new HashMap<>();

    /**
     * The conversation history mapped with a method name.
     */
    private Map<String, String> history = new HashMap<>();

    /**
     * The user that is leading the conversation.
     */
    private final String user;

    /**
     * The next stage in the conversation corresponding to the method name.
     */
    private String nextStage;

    /**
     * @param user The user that is leading the conversation
     * @param initialStage The initial conversation stage
     */
    private ConversationStore(String user, String initialStage) {
        this.user = user;
        this.nextStage = initialStage;
    }

    /**
     * Finds a message in the conversation history based on provided method name.
     *
     * @param stage The stage that the message is recovered from
     * @return The message from provided stage of conversation
     */
    public String history(String stage) {
        return this.history.get(stage);
    }

    /**
     * Skips to a provided stage in conversation by the method name.
     *
     * @param stage The stage to be skipped to
     */
    public void skip(String stage) {
        this.setNextStage(stage);
    }

    /**
     * Stops the conversation and removes it from the store.
     */
    public void stop() {
        openConversations.remove(this.user);
    }

    /**
     * Checks whether there is an open conversation for the provided user.
     *
     * @param user The user to find the conversation by
     * @return Whether the conversation exists for provided user
     */
    public static boolean exists(String user) {
        return openConversations.containsKey(user);
    }

    /**
     * Creates a new conversation for the provided user and sets the initial method.
     *
     * @param user The user to be assigned to the conversation.
     * @param initialMethod The initial method
     */
    public static void create(String user, String initialMethod) {
        openConversations.put(user, new ConversationStore(user, initialMethod));
    }

    /**
     * Finds a conversation based on provided user ID.
     *
     * @param user The user that the conversation is being found for
     * @return The conversation instance
     */
    public static ConversationStore find(String user) {
        return openConversations.get(user);
    }

    /**
     * Pushes a message event text to the history of conversation.
     *
     * @param stage The method name to be assigned with the message
     * @param message The message to be stored
     */
    public void pushMessageToHistory(String stage, String message) {
        this.history.put(stage, message);
    }

    /**
     * Returns the next stage for the conversation that is corresponding to
     * a dialogue method name.
     *
     * @return The next stage for the conversation
     */
    public String getNextStage() {
        return this.nextStage;
    }

    /**
     * Sets the next stage of the conversation, corresponding to the dialogue
     * method name.
     *
     * @param nextStage The next stage of the conversation
     */
    public void setNextStage(String nextStage) {
        this.nextStage = nextStage;
    }
}
