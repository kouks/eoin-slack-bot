package io.pavelkoch.eoin.rtm.events.concerns;

import io.pavelkoch.eoin.rtm.InteractsWithMessage;

public interface HasText extends InteractsWithMessage {
    /**
     * @return The text field from the json message
     */
    default String text() {
        return this.getMessage().getString("text");
    }
}
