package io.pavelkoch.eoin.rtm.events.concerns;

import io.pavelkoch.eoin.rtm.InteractsWithMessage;

public interface HasUser extends InteractsWithMessage {
    /**
     * @return The user field from the json message
     */
    default String user() {
        return this.getMessage().getString("user");
    }
}
