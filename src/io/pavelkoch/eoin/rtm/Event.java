package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.messaging.ResponseFactory;
import io.pavelkoch.eoin.modules.Module;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     * All available modules for the slack bot.
     */
    private ArrayList<Module> modules;

    /**
     * We set the Json object and the remote connection for the event to use
     * later on.
     *
     * @param json The Json object receive from the web socket
     * @param remote The remote web socket connection
     * @param modules All available modules for the slack bot
     */
    void with(JSONObject json, RemoteEndpoint.Basic remote, ArrayList<Module> modules) {
        this.json = json;
        this.remote = remote;
        this.modules = modules;
    }

    /**
     * Dispatches the event to all listener methods on the module.
     *
     * @param event The event that occurred
     */
    void dispatch(Events event) {
        for (Module module : this.modules) {
            this.dispatchForSuitableMethods(module.getClass().getMethods(), module, event);
        }
    }

    /**
     * Dispatches the event to annotated methods.
     *
     * @param methods All of the module class methods
     * @param module The module to be triggered
     * @param event The event that should be triggered
     */
    private void dispatchForSuitableMethods(Method[] methods, Module module, Events event) {
        for (Method method : methods) {
            if (method.getAnnotation(Listener.class).event() == event) {
                this.invokeMethod(method, module);
            }
        }
    }

    /**
     * Invokes a listener method on provided module.
     *
     * @param method The method to be invoked
     * @param module The module that is this method invoked on
     */
    private void invokeMethod(Method method, Module module) {
        try {
            method.invoke(module, this, this.getResponseFactory());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return A new response factory instance
     */
    private ResponseFactory getResponseFactory() {
        return new ResponseFactory(this.remote);
    }
}
