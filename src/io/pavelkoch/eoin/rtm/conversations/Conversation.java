package io.pavelkoch.eoin.rtm.conversations;

public interface Conversation {
    /**
     * Finds a message in the conversation history based on provided stage
     * corresponding to the dialogue method name.
     *
     * @param stage The stage that the message is recovered from
     * @return The message from provided stage of conversation
     */
    String history(String stage);

    /**
     * Skips to a provided stage in conversation corresponding to the method name.
     *
     * @param stage The stage to be skipped to
     */
    void skip(String stage);

    /**
     * Stops the conversation and removes it from the store.
     */
    void stop();
}
