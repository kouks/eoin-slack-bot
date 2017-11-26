package io.pavelkoch.eoin.http;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Response {
    /**
     * The data input stream from the response.
     */
    private final InputStream input;

    /**
     * @param input The data input stream from the response.
     */
    Response(InputStream input) {
        this.input = input;
    }

    /**
     * Parses the response data to a string.
     *
     * @return Raw string data from the response
     * @throws IOException When the buffered reader can't read a line from the input
     */
    public String get() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.input));
        StringBuilder response = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    /**
     * Returns a json object from the response.
     *
     * @return Json object
     * @throws IOException When the buffered reader can't read a line from the input
     */
    public JSONObject getJson() throws IOException {
        return new JSONObject(this.get());
    }
}
