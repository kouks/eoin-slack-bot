package io.pavelkoch.eoin.rtm.conversations;

import io.pavelkoch.eoin.rtm.events.Message;

public interface Conversation {
    /**
     * Finds a message in the conversation history based on provided stage
     * corresponding to the dialogue method name.
     *
     * @param stage The stage that the message is recovered from
     * @return The message from provided stage of conversation
     */
    Message history(String stage);

    /**
     * Syntax sugar for setting next stage.
     *
     * @param stage The stage to be skipped to
     */
    void skip(String stage);

    /**
     * Syntax sugar for setting next stage.
     *
     * @param stage The stage to be repeated
     */
    void repeat(String stage);

    /**
     * Stops the conversation and removes it from the store.
     */
    void stop();
}
