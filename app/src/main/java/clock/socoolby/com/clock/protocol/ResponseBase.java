package clock.socoolby.com.clock.protocol;

import org.json.JSONException;
import org.json.JSONObject;

class ResponseBase {
    int mResultCode = ProtocolConstants.RESULT_FAILED;



    public int getResultCode() {
        return mResultCode;
    }

    public boolean parseResponse(JSONObject object) {
        if (object == null) {
            return false;
        }
        try {
            mResultCode = object.optInt(ProtocolConstants.STR_RESULT_CODE, 0);
            if (mResultCode == ProtocolConstants.RESULT_OK) {
                return parse(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean parse(JSONObject object) throws JSONException {
        return true;
    }
}
