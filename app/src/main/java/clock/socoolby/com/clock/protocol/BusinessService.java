package clock.socoolby.com.clock.protocol;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import clock.socoolby.com.clock.ClockApplication;
import clock.socoolby.com.clock.utils.FuncUnit;
import clock.socoolby.com.clock.utils.NetworkService;

/**
 * Alway zuo,never die.
 * Created by socoolby on 5/26/16.
 */
public class BusinessService {
    private final static String TAG = BusinessService.class.getSimpleName();

    public void getWeather(String city) {
        if (!(city != null && !city.isEmpty()))
            return;
        WeatherRequest request = new WeatherRequest();
        request.setmCity(city);
        NetworkService.getInstance().sendRequest(request, new NetworkService.RequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                WeatherResponse weatherResponse = new WeatherResponse(response);
                try {
                    weatherResponse.parse(response);
                    if (weatherResponse.mResultCode == 0) {
                        ClockApplication.getInstance().getMainActivity().setWeather(weatherResponse);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestFailed(int error, String errorMessage) {
                Log.d(TAG, errorMessage);

            }
        });
    }

    public void checkUpdate() {
        UpdateRequest request = new UpdateRequest();
        NetworkService.getInstance().sendRequest(request, new NetworkService.RequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                UpdateResponse updateResponse = new UpdateResponse(response);
                if (updateResponse.getResultCode() == ProtocolConstants.RESULT_OK) {
                    FuncUnit.openURL(ClockApplication.getContext(), updateResponse.getUpdateURL());
                }

            }

            @Override
            public void onRequestFailed(int error, String errorMessage) {

            }
        });
    }
}
