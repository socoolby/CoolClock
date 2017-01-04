package clock.socoolby.com.clock.protocol;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class RequestBase {
    RequestBase() {
    }

    public String getUrl() {
        return "";
    }

    public JSONObject createRequest() {
        JSONObject object = new JSONObject();
        try {
            buildRequest(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    protected abstract void buildRequest(JSONObject object) throws JSONException;
}
