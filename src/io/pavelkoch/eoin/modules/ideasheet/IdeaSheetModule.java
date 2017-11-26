package io.pavelkoch.eoin.modules.ideasheet;

import io.pavelkoch.eoin.http.Request;
import io.pavelkoch.eoin.http.Response;
import io.pavelkoch.eoin.messaging.ResponseFactory;
import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.Events;
import io.pavelkoch.eoin.rtm.Listener;
import io.pavelkoch.eoin.rtm.events.Message;
import org.json.JSONObject;

import java.util.HashMap;

public class IdeaSheetModule extends Module {
    /**
     * @param event The event that this listener is assigned to
     * @param response The response factory
     */
    @Listener(event = Events.MESSAGE)
    public void onMessage(Message event, ResponseFactory response) {
        if (! event.text().toLowerCase().matches("^\\$.*")) {
            return;
        }

        try {
            Response httpResponse = new Request("http://idea.dev:8000/api/v1/ideas").post(new HashMap<String, String>() {{
                put("query", event.text());
                put("api_token", "KsdUaVejKmcIoJkNb7u3RiAu8jEO9l8SV0vMpO80fKwqqjmmosLyLcHoEEys");
            }});

            JSONObject idea = httpResponse.getJson();

            response.message("Hey, <@" + event.user() + ">, the idea was added! http://idea.dev:8000/ideas/" + idea.getInt("id"))
                    .channel(event.channel())
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}