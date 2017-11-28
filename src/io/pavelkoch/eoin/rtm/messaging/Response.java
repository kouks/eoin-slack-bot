package io.pavelkoch.eoin.rtm.messaging;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.util.function.Function;

public class Response {
    /**
     * The remote web socket connections we want to respond to.
     */
    private final RemoteEndpoint.Basic remote;

    /**
     * The soon-to-be response, instantiated as an empty Json object.
     */
    private JSONObject response = new JSONObject();

    /**
     * @param remote The remote web socket connection we want to respond to
     */
    public Response(RemoteEndpoint.Basic remote) {
        this.remote = remote;
    }

    /**
     * Adds the text field to the response.
     *
     * @param text The text to be added
     * @return This class for chaining
     */
    public Response text(String text) {
        this.response.put("type", "message");
        this.response.put("text", text);

        return this;
    }

    /**
     * Adds the channel field to the response.
     *
     * @param channel The channel be responded to
     * @return This class for chaining
     */
    public Response channel(String channel) {
        this.response.put("channel", channel);

        return this;
    }

    /**
     * Builds the attachment.
     *
     * @param callback That the attachment builder is sent to.
     * @return This class for chaining
     */
    public Response attach(Function<Attachment, Attachment> callback) {
        // If there are no attachments yet, create an empty json array
        if (! this.response.has("attachments")) {
            this.response.put("attachments", new JSONArray());
        }

        // Then add our attachment to the array.
        JSONArray attachments = this.response.getJSONArray("attachments");
        attachments.put(callback.apply(new Attachment()).toJson());

        return this;
    }

    /**
     * Sends the Json object to the remote web socket connection.
     */
    public void send() {
        try {
            this.remote.sendText(this.response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
