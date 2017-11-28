package io.pavelkoch.eoin.rtm;

import org.json.JSONObject;

public interface InteractsWithMessage {
    /**
     * @return The json message received through web sockets
     */
    JSONObject getMessage();
}
