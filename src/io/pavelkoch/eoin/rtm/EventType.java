package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.rtm.events.Hello;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.events.UserTyping;

public enum EventType {
    HELLO(Hello.class),
    MESSAGE(Message.class),
    USER_TYPING(UserTyping.class);

    /**
     * The event implementation class.
     */
    private final Class<? extends Event> event;

    /**
     * @param event The event implementation class
     */
    EventType(Class<? extends Event> event) {
        this.event = event;
    }

    /**
     * @return The event implementation class instance
     * @throws IllegalAccessException If the constructor is private
     * @throws InstantiationException If there was a problem instantiating the event implementation.
     */
    public Event getEvent() throws IllegalAccessException, InstantiationException {
        return this.event.newInstance();
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
