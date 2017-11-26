package io.pavelkoch.eoin.rtm.events;

import io.pavelkoch.eoin.rtm.Event;

public class UserTyping extends Event {

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
