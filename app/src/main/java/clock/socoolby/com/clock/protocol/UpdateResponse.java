package clock.socoolby.com.clock.protocol;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Alway zuo,never die.
 * Created by socoolby on 04/01/2017.
 */

public class UpdateResponse extends ResponseBase {
    private int mResultCode = ProtocolConstants.RESULT_FAILED;

    public int getmResultCode() {
        return mResultCode;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public String getDescription() {
        return description;
    }

    private String updateURL = null;
    private String description = null;

    UpdateResponse(JSONObject response) {
        super.parseResponse(response);
    }

    @Override
    protected boolean parse(JSONObject object) throws JSONException {
        if (object == null)
            return false;
//        object = new JSONObject("{\"message\":\"success\",\"code\":0,\"data\":{\"url\":\"http://www.baidu.com/\",\"description\":\"new version\"}}");
        mResultCode = object.optInt("code", ProtocolConstants.RESULT_FAILED);
        JSONObject data = object.optJSONObject("data");
        if (data != null) {
            updateURL = data.optString("url");
            description = data.optString("description");
        }
        return true;
    }
}
