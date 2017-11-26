package io.pavelkoch.eoin.rtm.events;

import io.pavelkoch.eoin.rtm.Event;
import io.pavelkoch.eoin.rtm.Listener;
import io.pavelkoch.eoin.rtm.listeners.UserTypingListener;

public class UserTyping extends Event {
    /**
     * Dispatches the event to a provided listener.
     */
    @Override
    public void dispatchTo(Listener listener) {
        ((UserTypingListener) listener).onUserTyping(this, this.getResponseFactory());
    }

    /**
     * @return The channel field from the Json object
     */
    public String channel() {
        return this.json.getString("channel");
    }

    /**
     * @return The user field from the Json object
     */
    public String user() {
        return this.json.getString("user");
    }
}
