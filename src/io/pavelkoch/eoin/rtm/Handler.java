package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.modules.Module;
import org.json.JSONObject;
import org.reflections.Reflections;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.ArrayList;

public class Handler implements MessageHandler.Whole<String> {
    /**
     * The current web socket session.
     */
    private final Session session;

    /**
     * The list of all available modules.
     */
    private ArrayList<Module> modules = new ArrayList<>();

    /**
     * @param session The current web socket session
     */
    Handler(Session session) {
        this.session = session;

        this.discoverModules();
    }

    /**
     * This method handles accepting the message as well as finding
     * the suitable event, calling the dispatch method on it then.
     *
     * @param rawMessage The message that is received through the web socket
     */
    @Override
    public void onMessage(String rawMessage) {
        System.out.println(rawMessage);

        // Creating a json object out of the received message
        JSONObject message = new JSONObject(rawMessage);

        // Finding the event type from the event type enum based
        // on the slack event type string
        EventType eventType = EventType.fromString(message.getString("type"));

        // If it is an event that corresponds to our event type enum,
        // fire this event for all of our modules.
        if (eventType != null) {
            try {
                eventType.getEvent().dispatch(eventType, message, this.session, this.modules);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Discovers all the slack bot modules by searching for any class that
     * is the subclass of Module.
     */
    private void discoverModules() {
        // To discover all available modules, we use the google reflections library,
        // looping through all subclasses of our abstract module class.
        for (Class <? extends Module> module : new Reflections().getSubTypesOf(Module.class)) {
            try {
                this.modules.add(module.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
