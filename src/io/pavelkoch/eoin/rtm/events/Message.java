package io.pavelkoch.eoin.rtm.events;

import io.pavelkoch.eoin.rtm.Event;

public class Message extends Event {
    /**
     * @return The text field from the json message
     */
    public String text() {
        return this.message.getString("text");
    }

    /**
     * @return The channel field from the json message
     */
    public String channel() {
        return this.message.getString("channel");
    }

    /**
     * @return The user field from the json message
     */
    public String user() {
        return this.message.getString("user");
    }
}
