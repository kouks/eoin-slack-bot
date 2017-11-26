package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.events.UserTyping;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.util.ArrayList;

public enum Events {
    MESSAGE("message", new Message()),
    USER_TYPING("user_typing", new UserTyping());

    /**
     * The event name corresponding to the slack event type.
     */
    private final String name;

    /**
     * The dispatchable class instance.
     */
    private final Event event;

    /**
     * @param name The event name corresponding to the slack event type
     * @param event The event class instance
     */
    Events(String name, Event event) {
        this.name = name;
        this.event = event;
    }

    /**
     * Assigns the json data from the slack message and a remote connection instance
     * to be dispatched with the event.
     *
     * @param json The json received from slack
     * @param remote The remote web socket connection
     * @param modules All available modules for the slack bot
     * @return This class for chaining
     */
    Events with(JSONObject json, RemoteEndpoint.Basic remote, ArrayList<Module> modules) {
        this.event.with(json, remote, modules);

        return this;
    }

    /**
     * Dispatches the event to all available listener methods.
     */
    void dispatch() {
        this.event.dispatch(this);
    }

    /**
     * @return The event name corresponding to the slack event type.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns an event instance corresponding to the slack event type.
     *
     * @param name The slack event name
     * @return Corresponding instance of this class
     */
    public static Events byName(String name) {
        for (Events event : Events.values()) {
            if (event.getName().equals(name)) {
                return event;
            }
        }

        return null;
    }
}
