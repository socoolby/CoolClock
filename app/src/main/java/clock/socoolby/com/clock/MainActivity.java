package clock.socoolby.com.clock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import clock.socoolby.com.clock.protocol.WeatherResponse;
import clock.socoolby.com.clock.utils.Constants;
import clock.socoolby.com.clock.utils.DateModel;
import clock.socoolby.com.clock.utils.Player;
import clock.socoolby.com.clock.utils.SharePerferenceModel;

public class MainActivity extends Activity implements Handler.Callback, View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int SETTING_REQUEST_CODE = 100;

    public final static int MODE_NORMAL = 200;
    public final static int MODE_SETTING_OTHER = 202;


    private TextView tv_time;
    private TextView tv_date;
    private TextView tv_day;
    private TextView tv_weather;
    private TextView tv_descript;
    private TextView tv_setting;

    private Timer timer;
    private Handler handler;
    private final static int UPDATE_TIME = 100;

    private PowerManager.WakeLock wakeLock = null;


    private SharePerferenceModel model;

    private PowerManager.WakeLock localWakeLock = null;


    public int mMode = MODE_NORMAL;

    public void setWeather(WeatherResponse weather) {
        if (weather == null)
            return;
        if (weather.getTodayWeather() != null)
            tv_weather.setText(weather.getTodayWeather().weather);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_day = (TextView) findViewById(R.id.tv_day);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/ds_digi.ttf");
        tv_time.setTypeface(typeFace);
        tv_date.setTypeface(typeFace);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_descript = (TextView) findViewById(R.id.tv_descript);


        handler = new Handler(this);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        RelativeLayout rel_main = (RelativeLayout) findViewById(R.id.rel_main);
        rel_main.setOnClickListener(this);
        init();
        PowerManager powerManager = (PowerManager) this.getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Clock");
        localWakeLock = powerManager.newWakeLock(32, "MyPower");

        ClockApplication.getInstance().setMainActivity(this);
        ClockApplication.getInstance().getBusinessService().getWeather(model.getCity());

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(UPDATE_TIME);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        ClockApplication.getInstance().getBusinessService().checkUpdate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void init() {
        model = new SharePerferenceModel();
        model.read();
        Log.d(TAG, "model:" + model.toString());
        if (model.getDescription() != null) {
            tv_descript.setText(model.getDescription());
        }
        Intent startIntent = new Intent(this, ProximityService.class);
        if (model.isTriggerScreen()) {
            startService(startIntent);
        } else {
            stopService(startIntent);
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        wakeLock.acquire();
    }


    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, SETTING_REQUEST_CODE);
                tv_setting.setVisibility(View.GONE);
                break;
            case R.id.rel_main:
                localWakeLock.isHeld();
                switchMode(MODE_SETTING_OTHER);
                break;
            case R.id.tv_time:
                switchMode(MODE_SETTING_OTHER);
                break;
        }
    }

    private void switchMode(int mode) {
        switch (mode) {
            case MODE_NORMAL:
                tv_setting.setVisibility(View.GONE);
                break;
            case MODE_SETTING_OTHER:
                tv_setting.setVisibility(View.VISIBLE);
                break;
        }
        mMode = mode;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switchMode(MODE_NORMAL);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, SETTING_REQUEST_CODE);
            tv_setting.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUEST_CODE) {
            init();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE_TIME:
                updateTime();
                break;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void updateTime() {

        DateModel date = new DateModel();
        String dateString = date.getDateString();
        String dayString = date.getToday();
        String timeString = model.isDisplaySecond() ? date.getTimeString() : date.getShortTimeString();
        timeString=timeString.replace("1", " 1");
        if(timeString.startsWith(" "))
            timeString=timeString.substring(1,timeString.length());
        tv_time.setText(timeString);
        tv_date.setText(dateString.replace("1", " 1"));
        tv_day.setText(dayString);
        reportTime(date);
    }

    private boolean isReport(int hour, int minute) {

        DateModel startTime = model.getStartHourPowerTime();
        DateModel stopTime = model.getStopHourPowerTime();
        DateModel nowTime = new DateModel();
        nowTime.setTime(hour, minute);

        if (startTime.getShortTimeString().equals(stopTime.getShortTimeString()))
            return true;
        long minutes = startTime.minusTime(stopTime);
        if (minutes < 0) {//stop>start
            if (nowTime.minusTime(startTime) >= 0 && nowTime.minusTime(stopTime) <= 0) {
                return false;
            }
        }
        return true;
    }

    private void reportTime(DateModel date) {
        if (model.isTickSound()) {
            Player.getInstance().playTick(this);
        }
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        int hour = date.getHour();
        int minute = date.getMinute();
        int second = date.getSecond();
        int today = date.getWeek();

        if (model.getTypeHourPower() != Constants.TALKING_NO_REPORT) {
            if ((minute == 30 || minute == 0) && model.getTypeHourPower() == Constants.TALKING_HALF_AN_HOUR && second == 0) {
                Log.d(TAG, String.format("reportTime Year:%d Month:%d Day:%d Hour:%d Minute:%d Today:%d", year, month, day, hour, minute, today));
                if (isReport(hour, minute))
                    Player.getInstance().reportTime(this, year, month, day, hour, minute, today);
            } else if (model.getTypeHourPower() == Constants.TALKING_HOURS && minute == 0 && second == 0) {
                if (isReport(hour, minute))
                    Player.getInstance().reportTime(this, year, month, day, hour, minute, today);
            }
        }

    }
}
