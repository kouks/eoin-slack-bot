package io.pavelkoch.eoin.rtm.events.concerns;

import io.pavelkoch.eoin.rtm.InteractsWithMessage;

public interface HasChannel extends InteractsWithMessage {
    /**
     * @return The channel field from the json message
     */
    default String channel() {
        return this.getMessage().getString("channel");
    }
}
