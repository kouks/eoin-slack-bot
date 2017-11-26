package io.pavelkoch.eoin.rtm.events;

import io.pavelkoch.eoin.rtm.Event;

public class Message extends Event {
    /**
     * @return The text field from the Json object
     */
    public String text() {
        return this.json.getString("text");
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
