package io.pavelkoch.eoin.rtm.conversations;

public interface Conversation {
    String history(String stage);
    void skip(String stage);
    void stop();
}
