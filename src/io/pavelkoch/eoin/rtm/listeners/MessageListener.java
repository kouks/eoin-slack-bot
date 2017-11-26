package io.pavelkoch.eoin.rtm.listeners;

import io.pavelkoch.eoin.messaging.ResponseFactory;
import io.pavelkoch.eoin.rtm.Listener;
import io.pavelkoch.eoin.rtm.events.Message;

public interface MessageListener extends Listener {
    /**
     * Event triggered by a user sending a message.
     *
     * @param event The event that this listener is assigned to
     * @param response The response factory
     */
    void onMessage(Message event, ResponseFactory response);
}
