package io.pavelkoch.eoin.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class Request {
    /**
     * The http connection.
     */
    private final HttpURLConnection http;

    /**
     * @param url The URl to request
     * @throws IOException If the URL format is invalid
     */
    public Request(String url) throws IOException {
        this.http = (HttpURLConnection) new URL(url).openConnection();
    }

    /**
     * Sending an HTTP POST request.
     *
     * @param data Data to be sent
     * @return Response from the request
     * @throws IOException If the output stream does not accept the data
     */
    public Response post(Map<String, String> data) throws IOException {
        this.http.setDoOutput(true);
        this.http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        return this.request("POST", data);
    }

    /**
     * This method actually performs the request, other ones are just syntactic sugar,
     * so we don't have to be explicit about the request method.
     *
     * @param method The method to use when requesting
     * @param data Data to be sent
     * @return Response from the request
     * @throws IOException If the output stream does not accept the data
     */
    private Response request(String method, Map<String, String> data) throws IOException {
        this.http.setRequestMethod(method);
        this.http.connect();
        this.http.getOutputStream().write(this.parseData(data));

        return new Response(this.http.getInputStream());
    }

    /**
     * This method transforms the data map to an array of bytes, which
     * is acceptable by the request library.
     *
     * @param data Data to be parsed
     * @return Data parsed for the request
     * @throws IOException If the charset is not valid
     */
    private byte[] parseData(Map<String, String> data) throws IOException {
        StringJoiner joiner = new StringJoiner("&");

        for (Map.Entry<String, String> entry : data.entrySet()) {
            joiner.add(this.encodeParams(entry.getKey(), entry.getValue()));
        }

        return joiner.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * This method makes sure to escape our params for the request.
     *
     * @param key The key to be encoded
     * @param value The value to be encoded
     * @return The encoded key-value pair
     * @throws IOException If the charset is not valid
     */
    private String encodeParams(String key, String value) throws IOException {
        return URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
    }
}
