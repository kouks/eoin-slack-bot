package io.pavelkoch.eoin.messaging;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.util.function.Function;

public class ResponseFactory {
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
    public ResponseFactory(RemoteEndpoint.Basic remote) {
        this.remote = remote;
    }

    /**
     * Adds the message field to the response.
     *
     * @param message The message to be added
     * @return This class for chaining
     */
    public ResponseFactory message(String message) {
        this.response.put("type", "message");
        this.response.put("text", message);

        return this;
    }

    /**
     * Adds the channel field to the response.
     *
     * @param channel The channel be responded to
     * @return This class for chaining
     */
    public ResponseFactory channel(String channel) {
        this.response.put("channel", channel);

        return this;
    }

    /**
     * Builds the attachment.
     *
     * @param callback That the attachment builder is sent to.
     * @return This class for chaining
     */
    public ResponseFactory attach(Function<Attachment, Attachment> callback) {
        JSONArray attachment = new JSONArray().put(callback.apply(new Attachment()).toJson());

        this.response.put("attachments", attachment);

        return this;
    }

    /**
     * Sends the Json object to the remote web socket connection.
     */
    public void send() {
        try {
            System.out.println(this.response.toString());
            this.remote.sendText(this.response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
