package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.modules.Module;

import java.util.ArrayList;

public interface Dispatcher {
    /**
     * Loops through all available modules and dispatches events to suitable methods
     * on those modules.
     *
     * @param eventType The dispatched event type
     * @param event The dispatched event implementation
     * @param modules All available modules to be dispatched to
     */
    void dispatch(EventType eventType, Event event, ArrayList<Module> modules);
}
