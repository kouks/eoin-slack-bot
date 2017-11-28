package io.pavelkoch.eoin.modules.ideasheet;

import io.pavelkoch.eoin.http.Request;
import io.pavelkoch.eoin.rtm.messaging.Response;
import io.pavelkoch.eoin.modules.Module;
import io.pavelkoch.eoin.rtm.EventType;
import io.pavelkoch.eoin.rtm.Controller;
import io.pavelkoch.eoin.rtm.events.Message;
import org.json.JSONObject;

import java.util.HashMap;

public class IdeasheetModule extends Module {
    /**
     * Crates a new idea on the ideasheet app.
     *
     * @param event The event that this controller listens for
     * @param response The response factory
     */
    @Controller(event = EventType.MESSAGE, pattern = "^\\$.*")
    public void createIdea(Message event, Response response) {
        try {
            io.pavelkoch.eoin.http.Response httpResponse = new Request("http://idea.dev:8000/api/v1/ideas").post(new HashMap<String, String>() {{
                put("query", event.text());
                put("api_token", "KsdUaVejKmcIoJkNb7u3RiAu8jEO9l8SV0vMpO80fKwqqjmmosLyLcHoEEys");
            }});

            JSONObject idea = httpResponse.getJson();

            response.text("Hey, <@" + event.user() + ">, the idea was added! http://idea.dev:8000/ideas/" + idea.getInt("id"))
                    .channel(event.channel())
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
