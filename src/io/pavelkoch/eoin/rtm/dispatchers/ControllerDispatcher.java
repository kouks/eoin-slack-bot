package io.pavelkoch.eoin.rtm.dispatchers;

import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.Controller;
import io.pavelkoch.eoin.rtm.Dispatcher;
import io.pavelkoch.eoin.rtm.Event;
import io.pavelkoch.eoin.rtm.EventType;
import io.pavelkoch.eoin.rtm.events.concerns.HasText;
import io.pavelkoch.eoin.rtm.messaging.Response;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ControllerDispatcher implements Dispatcher {
    /**
     * The type type that is being dispatched.
     */
    private EventType eventType;

    /**
     * The event implementation that is being dispatched.
     */
    private Event event;

    /**
     * Loops through all available modules and dispatches events to suitable controllers
     * on those modules.
     *
     * @param eventType The type type that is being dispatched
     * @param event The event implementation that is being dispatched
     * @param modules All available modules to be dispatched to
     */
    @Override
    public void dispatch(EventType eventType, Event event, ArrayList<Module> modules) {
        this.eventType = eventType;
        this.event = event;

        for (Module module : modules) {
            this.dispatchForSuitableControllers(module.getClass().getMethods(), module);
        }
    }

    /**
     * Dispatches the event to controllers.
     *
     * @param methods All of the module class methods
     * @param module The module to be triggered
     */
    private void dispatchForSuitableControllers(Method[] methods, Module module) {
        for (Method method : methods) {
            Controller annotation = method.getAnnotation(Controller.class);

            if (
                    this.isController(annotation) &&
                    this.controllerSuitableForEvent(annotation.event()) &&
                    this.controllerWantsPattern(annotation.pattern())
            ) {
                this.invokeController(method, module);
            }
        }
    }

    /**
     * Checks whether the method is annotated as a controller.
     *
     * @param annotation The annotation to be checked
     * @return If the method is annotated as controller
     */
    private boolean isController(Controller annotation) {
        return annotation != null;
    }

    /**
     * Checks whether the controllers wants to react to this event.
     *
     * @param eventType The event type to be checked
     * @return If the controller accepts this event
     */
    private boolean controllerSuitableForEvent(EventType eventType) {
        return eventType == this.eventType;
    }

    /**
     * Determines whether the controller accepts a text based on provided pattern.
     *
     * @param pattern The pattern
     * @return If the controller accepts the text
     */
    private boolean controllerWantsPattern(String pattern) {
        return !(this.event instanceof HasText) || ((HasText) this.event).text().matches(pattern);
    }

    /**
     * Invokes a controller for a module.
     *
     * @param controller The method to be invoked
     * @param module The module that is this method invoked on
     */
    private void invokeController(Method controller, Module module) {
        try {
            controller.invoke(module, this.event, new Response(this.event.session().getBasicRemote()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
