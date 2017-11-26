package io.pavelkoch.eoin.messaging;

import org.json.JSONObject;

public class Attachment {

    private JSONObject attachment = new JSONObject();

    public Attachment title(String title) {
        this.attachment.put("title", title);

        return this;
    }

    public Attachment text(String text) {
        this.attachment.put("text", text);

        return this;
    }

    public JSONObject toJson() {
        return this.attachment;
    }
}
