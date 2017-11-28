package io.pavelkoch.eoin;

import io.pavelkoch.eoin.config.Config;
import io.pavelkoch.eoin.exceptions.Handler;
import io.pavelkoch.eoin.http.Request;
import io.pavelkoch.eoin.http.Response;
import io.pavelkoch.eoin.rtm.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class Eoin {
    /**
     * The program entry point.
     *
     * @param args The console arguments
     */
    public static void main(String[] args) {
        Logger.getLogger("eoin").info("asd");

        try {
            // We boot our glorious slack bot.
            new Eoin().boot();
        } catch (Exception e) {
            Handler.render(e);
        }
    }

    private void boot() throws Exception {
        Config config = new Config().load();

        // We send an http request to slack, getting the web socket URI to work with
        Response response = new Request(config.get("RTM_CONNECT_URL")).post(new HashMap<String, String>() {{
            put("token", config.get("BOT_TOKEN"));
        }});

        // We attempt to establish the web socket connection based
        // on the provided URI.
        Client client = new Client(response.getJson().getString("url"));
        client.connect();
    }
}
