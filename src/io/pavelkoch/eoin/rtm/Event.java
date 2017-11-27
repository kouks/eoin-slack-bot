package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.messaging.ResponseFactory;
import io.pavelkoch.eoin.modules.Module;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class Event {
    /**
     * The json message received from the web socket.
     */
    protected JSONObject message;

    /**
     * The current web socket session.
     */
    private Session session;

    /**
     * @return The current web socket session
     */
    public Session session() {
        return this.session;
    }

    /**
     * Dispatches the event to all suitable controllers.
     *
     * @param eventType The event type that occurred
     * @param message The message message received from the web socket
     * @param session The current web socket session
     * @param modules All available modules for the slack bot
     */
    void dispatch(EventType eventType, JSONObject message, Session session, ArrayList<Module> modules) {
        this.message = message;
        this.session = session;

        for (Module module : modules) {
            this.dispatchForSuitableControllers(module.getClass().getMethods(), module, eventType);
        }
    }

    /**
     * Dispatches the event to controllers.
     *
     * @param methods All of the module class methods
     * @param module The module to be triggered
     * @param eventType The event type that should be triggered
     */
    private void dispatchForSuitableControllers(Method[] methods, Module module, EventType eventType) {
        for (Method method : methods) {
            if (this.methodIsControllerFor(eventType, method) && this.controllerWantsPattern(method)) {
                this.invokeController(method, module);
            }
        }
    }

    /**
     * Determines whether the method is a suitable controller for provided event.
     *
     * @param eventType The event type to be matched
     * @param method The wannabe controller
     * @return If the method is a suitable controller
     */
    private boolean methodIsControllerFor(EventType eventType, Method method) {
        return method.getAnnotation(Controller.class).event() == eventType;
    }

    /**
     * Determines whether the controllers accepts a message based on provided pattern.
     *
     * @param controller The controller
     * @return If the controller accepts the message
     */
    private boolean controllerWantsPattern(Method controller) {
        return !this.message.has("text") || this.message.getString("text").matches(controller.getAnnotation(Controller.class).pattern());

    }

    /**
     * Invokes a controller for a module.
     *
     * @param controller The method to be invoked
     * @param module The module that is this method invoked on
     */
    private void invokeController(Method controller, Module module) {
        try {
            controller.invoke(module, this, new ResponseFactory(this.session.getBasicRemote()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
