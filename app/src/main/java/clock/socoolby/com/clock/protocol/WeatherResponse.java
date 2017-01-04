package clock.socoolby.com.clock.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherResponse extends ResponseBase {
    /*
    {
	"error": 0,
	"status": "success",
	"date": "2016-05-26",
	"results": [{
		"currentCity": "深圳",
		"pm25": "31",
		"index": [{
			"title": "穿衣",
			"zs": "热",
			"tipt": "穿衣指数",
			"des": "天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"
		},
		{
			"title": "洗车",
			"zs": "不宜",
			"tipt": "洗车指数",
			"des": "不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"
		},
		{
			"title": "旅游",
			"zs": "较不宜",
			"tipt": "旅游指数",
			"des": "温度适宜，风力不大，但预计将有有强降水出现，会给您的出游增添很多麻烦，建议您最好选择室内活动。"
		},
		{
			"title": "感冒",
			"zs": "较易发",
			"tipt": "感冒指数",
			"des": "天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。"
		},
		{
			"title": "运动",
			"zs": "较不宜",
			"tipt": "运动指数",
			"des": "有较强降水，建议您选择在室内进行健身休闲运动。"
		},
		{
			"title": "紫外线强度",
			"zs": "弱",
			"tipt": "紫外线强度指数",
			"des": "紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"
		}],
		"weather_data": [{
			"date": "周四 05月26日 (实时：28℃)",
			"dayPictureUrl": "http://api.map.baidu.com/images/weather/day/dayu.png",
			"nightPictureUrl": "http://api.map.baidu.com/images/weather/night/dayu.png",
			"weather": "大雨",
			"wind": "微风",
			"temperature": "31 ~ 25℃"
		},
		{
			"date": "周五",
			"dayPictureUrl": "http://api.map.baidu.com/images/weather/day/baoyu.png",
			"nightPictureUrl": "http://api.map.baidu.com/images/weather/night/leizhenyu.png",
			"weather": "暴雨转雷阵雨",
			"wind": "微风",
			"temperature": "29 ~ 25℃"
		},
		{
			"date": "周六",
			"dayPictureUrl": "http://api.map.baidu.com/images/weather/day/leizhenyu.png",
			"nightPictureUrl": "http://api.map.baidu.com/images/weather/night/zhenyu.png",
			"weather": "雷阵雨转阵雨",
			"wind": "微风",
			"temperature": "30 ~ 25℃"
		},
		{
			"date": "周日",
			"dayPictureUrl": "http://api.map.baidu.com/images/weather/day/zhenyu.png",
			"nightPictureUrl": "http://api.map.baidu.com/images/weather/night/zhenyu.png",
			"weather": "阵雨",
			"wind": "微风",
			"temperature": "31 ~ 26℃"
		}]
	}]
}
     */
    private int mErrorCode;
    private String mCurrentCity;
    private int mPM25;
    private Weather todayWeather;

    public class Weather {
        public String date;
        public String weather;
        public String wind;
        public String temperature;

        public void parse(JSONObject jsonObject) {
            try {
                date = jsonObject.getString("date");
                weather = jsonObject.getString("weather");
                wind = jsonObject.getString("wind");
                temperature = jsonObject.getString("temperature");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public int getmErrorCode() {
        return mErrorCode;
    }

    public void setmErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getmCurrentCity() {
        return mCurrentCity;
    }

    public void setmCurrentCity(String mCurrentCity) {
        this.mCurrentCity = mCurrentCity;
    }

    public int getmPM25() {
        return mPM25;
    }

    public void setmPM25(int mPM25) {
        this.mPM25 = mPM25;
    }

    public Weather getTodayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(Weather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public WeatherResponse(JSONObject response) {
        super.parseResponse(response);
    }

    @Override
    protected boolean parse(JSONObject object) throws JSONException {
        mErrorCode = object.getInt("error");
        if (mErrorCode == 0) {
            JSONArray results = object.getJSONArray("results");
            JSONObject summary = (JSONObject) results.get(0);
            mCurrentCity = summary.getString("currentCity");
            mPM25 = summary.getInt("pm25");
            JSONArray weathers = summary.getJSONArray("weather_data");
            todayWeather = new Weather();
            JSONObject todayJsonObject = (JSONObject) weathers.get(0);
            todayWeather.parse(todayJsonObject);
        }
        return true;
    }


}
