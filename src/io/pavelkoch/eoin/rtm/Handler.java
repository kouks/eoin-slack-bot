package io.pavelkoch.eoin.rtm;

import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.modules.test.IdeaSheetModule;
import org.json.JSONObject;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.ArrayList;

public class Handler implements MessageHandler.Whole<String> {
    /**
     * The remote web socket connection.
     */
    private final Session session;

    /**
     * The remote web socket connection.
     */
    private final ArrayList<Module> modules = new ArrayList<>();

    /**
     * @param session The web socket session
     */
    Handler(Session session) {
        this.session = session;

        this.registerModules();
    }

    /**
     * This method handles accepting the message as well as finding
     * the suitable event, calling the dispatch method on it then.
     *
     * @param message The message that is received through the web socket
     */
    @Override
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        Events event = Events.byName(json.getString("type"));

        System.out.println(message);

        if (event != null) {
            event.with(json, this.session.getBasicRemote()).dispatch(this.modules);
        }
    }

    /**
     * We register all available listeners.
     */
    private void registerModules() {
        modules.add(new IdeaSheetModule());
    }
}
