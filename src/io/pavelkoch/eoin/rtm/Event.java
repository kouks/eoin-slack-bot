package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.rtm.dispatchers.ControllerDispatcher;
import io.pavelkoch.eoin.rtm.dispatchers.ConversationDispatcher;
import io.pavelkoch.eoin.rtm.events.Message;
import io.pavelkoch.eoin.rtm.messaging.Response;
import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.events.concerns.HasText;
import org.json.JSONObject;

import javax.websocket.Session;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Event implements InteractsWithMessage {
    /**
     * The json message received from the web socket.
     */
    protected JSONObject message;

    /**
     * The current web socket session.
     */
    private Session session;

    /**
     * All available dispatcher types.
     */
    private List<Dispatcher> dispatchers = new ArrayList<Dispatcher>() {{
        add(new ControllerDispatcher());
        add(new ConversationDispatcher());
    }};

    /**
     * Dispatches the event to all suitable controllers.
     *
     * @param eventType The event type that occurred
     * @param message The json message received from the web socket
     * @param session The current web socket session
     * @param modules All available modules for the slack bot
     */
    void dispatch(EventType eventType, JSONObject message, Session session, ArrayList<Module> modules) {
        this.message = message;
        this.session = session;

        for (Dispatcher dispatcher : this.dispatchers) {
            dispatcher.dispatch(eventType, this, modules);
        }
    }

    /**
     * @return The current web socket session
     */
    public Session session() {
        return this.session;
    }

    /**
     * @return The json message received through web sockets
     */
    @Override
    public JSONObject getMessage() {
        return this.message;
    }
}
