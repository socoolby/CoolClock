package clock.socoolby.com.clock.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import clock.socoolby.com.clock.ClockApplication;
import clock.socoolby.com.clock.R;

public class SharePerferenceModel implements Serializable {
    private int typeHourPower = Constants.TALKING_HALF_AN_HOUR;
    private final static String KEY_TYPE_HOUR_POWER = "key_type_hour_power";

    private DateModel startHourPowerTime = null;
    private final static String KEY_START_POWER = "key_start_power";
    private DateModel stopHourPowerTime = null;
    private final static String KEY_STOP_POWER = "key_stop_power";

    private boolean isDisplaySecond = true;
    private final static String KEY_IS_DISPLAY_SECOND = "key_is_display_second";
    private boolean isTickSound = true;
    private final static String KEY_IS_TICK_SOUND = "key_is_tick_sound";
    private boolean isTriggerScreen = true;
    private final static String KEY_IS_TRIGGER_SCREEN = "key_is_trigger_screen";


    private final static String KEY_CITY = "key_city";
    private String mCity;

    private final static String KEY_DESCRPTION = "key_description";
    private String mDescription;

    private final static String KEY_DISPLAYVIEW_TIME = "key_displayview_time";
    private final static String KEY_DISPLAYVIEW_DATE = "key_dsplayview_date";
    private final static String KEY_DISPLAYVIEW_DAY = "key_displayview_day";
    private final static String KEY_DISPLAYVIEW_WEATHER = "key_displayview_weather";
    private final static String KEY_DISPLAYVIEW_DESCRIPTION = "key_displayview_description";
    private JSONObject timeLocation = new JSONObject();
    private JSONObject dateLocation = new JSONObject();
    private JSONObject dayLocation = new JSONObject();
    private JSONObject weatherLocation = new JSONObject();
    private JSONObject descriptionLocation = new JSONObject();


    public int getTypeHourPower() {
        return typeHourPower;
    }

    public void setTypeHourPower(int typeHourPower) {
        this.typeHourPower = typeHourPower;
    }

    public DateModel getStartHourPowerTime() {
        return startHourPowerTime;
    }


    public void setStartHourPowerTime(DateModel startHourPowerTime) {
        this.startHourPowerTime = startHourPowerTime;
    }

    public DateModel getStopHourPowerTime() {
        return stopHourPowerTime;
    }

    public void setStopHourPowerTime(DateModel stopHourPowerTime) {
        this.stopHourPowerTime = stopHourPowerTime;
    }

    public boolean isDisplaySecond() {
        return isDisplaySecond;
    }

    public void setDisplaySecond(boolean displaySecond) {
        isDisplaySecond = displaySecond;
    }

    public boolean isTickSound() {
        return isTickSound;
    }

    public void setTickSound(boolean tickSound) {
        isTickSound = tickSound;
    }

    public boolean isTriggerScreen() {
        return isTriggerScreen;
    }

    public void setTriggerScreen(boolean triggerScreen) {
        isTriggerScreen = triggerScreen;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setTimeLocation(JSONObject timeLocation) {
        this.timeLocation = timeLocation;
    }

    public void setDateLocation(JSONObject dateLocation) {
        this.dateLocation = dateLocation;
    }

    public void setDayLocation(JSONObject dayLocation) {
        this.dayLocation = dayLocation;
    }

    public void setWeatherLocation(JSONObject weatherLocation) {
        this.weatherLocation = weatherLocation;
    }

    public void setDescriptionLocation(JSONObject descriptionLocation) {
        this.descriptionLocation = descriptionLocation;
    }

    public JSONObject getTimeLocation() {
        return timeLocation;
    }

    public JSONObject getDateLocation() {
        return dateLocation;
    }

    public JSONObject getDayLocation() {
        return dayLocation;
    }

    public JSONObject getWeatherLocation() {
        return weatherLocation;
    }

    public JSONObject getDescriptionLocation() {
        return descriptionLocation;
    }

    private void fromJsonString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            typeHourPower = jsonObject.getInt(KEY_TYPE_HOUR_POWER);
            isDisplaySecond = jsonObject.getBoolean(KEY_IS_DISPLAY_SECOND);
            isTickSound = jsonObject.getBoolean(KEY_IS_TICK_SOUND);
            isTriggerScreen =jsonObject.optBoolean(KEY_IS_TRIGGER_SCREEN,true);
            mCity = jsonObject.getString(KEY_CITY);
            mDescription = jsonObject.optString(KEY_DESCRPTION, ClockApplication.getContext().getResources().getString(R.string.always_zuo_never_die));
            startHourPowerTime = new DateModel();
            startHourPowerTime.setDataString(jsonObject.getString(KEY_START_POWER));
            stopHourPowerTime = new DateModel();
            stopHourPowerTime.setDataString(jsonObject.getString(KEY_STOP_POWER));
            timeLocation = new JSONObject(jsonObject.getString(KEY_DISPLAYVIEW_TIME));
            dateLocation = new JSONObject(jsonObject.getString(KEY_DISPLAYVIEW_DATE));
            dayLocation = new JSONObject(jsonObject.getString(KEY_DISPLAYVIEW_DAY));
            weatherLocation = new JSONObject(jsonObject.getString(KEY_DISPLAYVIEW_WEATHER));
            descriptionLocation = new JSONObject(jsonObject.getString(KEY_DISPLAYVIEW_DESCRIPTION));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_TYPE_HOUR_POWER, typeHourPower);
            jsonObject.put(KEY_IS_DISPLAY_SECOND, isDisplaySecond);
            jsonObject.put(KEY_IS_TICK_SOUND, isTickSound);
            jsonObject.put(KEY_IS_TRIGGER_SCREEN, isTriggerScreen);
            jsonObject.put(KEY_CITY, mCity);
            jsonObject.put(KEY_DESCRPTION, mDescription);
            jsonObject.put(KEY_START_POWER, startHourPowerTime.getTime());
            jsonObject.put(KEY_STOP_POWER, stopHourPowerTime.getTime());

            jsonObject.put(KEY_DISPLAYVIEW_TIME, timeLocation.toString());
            jsonObject.put(KEY_DISPLAYVIEW_DATE, dateLocation.toString());
            jsonObject.put(KEY_DISPLAYVIEW_DAY, dayLocation.toString());
            jsonObject.put(KEY_DISPLAYVIEW_WEATHER, weatherLocation.toString());
            jsonObject.put(KEY_DISPLAYVIEW_DESCRIPTION, descriptionLocation.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public void save() {
        FileUtils.writeObject(Constants.SHARE_PERFERENCE_FILE, toJsonString());
    }

    public void read() {
        fromJsonString((String) FileUtils.readObject(Constants.SHARE_PERFERENCE_FILE));
    }


    @Override
    public String toString() {
        return "SharePerferenceModel{" +
                "typeHourPower=" + typeHourPower +
                ", startHourPowerTime=" + startHourPowerTime +
                ", stopHourPowerTime=" + stopHourPowerTime +
                ", isDisplaySecond=" + isDisplaySecond +
                ", isTickSound=" + isTickSound +
                ", mCity='" + mCity + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", timeLocation=" + timeLocation +
                ", dateLocation=" + dateLocation +
                ", dayLocation=" + dayLocation +
                ", weatherLocation=" + weatherLocation +
                ", descriptionLocation=" + descriptionLocation +
                '}';
    }
}
