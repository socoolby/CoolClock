package clock.socoolby.com.clock.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import clock.socoolby.com.clock.utils.FuncUnit;


class UpdateRequest extends RequestBase {
    @Override
    public String getUrl() {
        return "http://www.socoolby.com/clock/update";
    }

    @Override
    protected void buildRequest(JSONObject object) throws JSONException {
        object.put("version", FuncUnit.getVersionName(FuncUnit.getBoxPackageName()));
    }
}
