package io.pavelkoch.eoin.rtm.listeners;

import io.pavelkoch.eoin.messaging.ResponseFactory;
import io.pavelkoch.eoin.rtm.Listener;
import io.pavelkoch.eoin.rtm.events.UserTyping;

public interface UserTypingListener extends Listener {
    /**
     * Event triggered by a user typing a message.
     *
     * @param event The event that this listener is assigned to
     * @param response The response factory
     */
    void onUserTyping(UserTyping event, ResponseFactory response);
}
