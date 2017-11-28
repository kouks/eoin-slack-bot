package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.rtm.events.Hello;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.events.UserTyping;

public enum EventType {
    HELLO(new Hello()),
    MESSAGE(new Message()),
    USER_TYPING(new UserTyping());

    /**
     * The event implementation class instance.
     */
    private final Event event;

    /**
     * @param event The event implementation class instance
     */
    EventType(Event event) {
        this.event = event;
    }

    /**
     * @return The event implementation class instance
     */
    public Event getEvent() {
        return this.event;
    }

    /**
     * Returns an event type instance corresponding to the slack event type name.
     *
     * @param name The slack event name
     * @return The corresponding event type
     */
    public static EventType fromString(String name) {
        for (EventType event : EventType.values()) {
            if (event.toString().equals(name.toUpperCase())) {
                return event;
            }
        }

        return null;
    }
}
