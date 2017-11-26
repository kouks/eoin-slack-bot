package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.messaging.ResponseFactory;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.util.ArrayList;

public abstract class Event {
    /**
     * The Json object received from the web socket
     */
    protected JSONObject json;

    /**
     * The remote web socket connection.
     */
    private RemoteEndpoint.Basic remote;

    /**
     * The array of event listeners.
     */
    private ArrayList<Listener> listeners = new ArrayList<>();

    /**
     * We set the Json object and the remote connection for the event to use
     * later on.
     *
     * @param json The Json object receive from the web socket
     * @param remote The remote web socket connection
     */
    void with(JSONObject json, RemoteEndpoint.Basic remote) {
        this.json = json;
        this.remote = remote;
    }

    /**
     * @param listener Listened to be added.
     */
    void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Dispatches the event to all available listeners.
     */
    void dispatch() {
        for (Listener listener : this.listeners) {
            this.dispatchTo(listener);
        }
    }

    /**
     * @return A new response factory instance
     */
    protected ResponseFactory getResponseFactory() {
        return new ResponseFactory(this.remote);
    }

    /**
     * Dispatches the event to a provided listener.
     *
     * @param listener The provided.
     */
    public abstract void dispatchTo(Listener listener);
}
