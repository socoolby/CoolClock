package clock.socoolby.com.clock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import clock.socoolby.com.clock.utils.DateModel;
import clock.socoolby.com.clock.utils.FuncUnit;
import clock.socoolby.com.clock.utils.SharePerferenceModel;
import clock.socoolby.com.clock.widget.WheelView;
import clock.socoolby.com.clock.utils.Constants;
import clock.socoolby.com.clock.widget.adapters.ArrayWheelAdapter;

public class SettingActivity extends Activity implements View.OnClickListener {
    private WheelView weel_startTime;
    private WheelView weel_stopTime;
    RadioButton rb_halfhour;
    RadioButton rb_hours;
    RadioButton rb_noreport;
    private EditText et_description;
    private EditText et_city;


    private SharePerferenceModel model;

    String[] listTime = new String[48];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RadioGroup rg_taking_clock = (RadioGroup) findViewById(R.id.rg_talking_clock);
        rb_halfhour = (RadioButton) findViewById(R.id.rb_halfhour);
        rb_hours = (RadioButton) findViewById(R.id.rb_hours);
        rb_noreport = (RadioButton) findViewById(R.id.rb_noreport);
        weel_startTime = (WheelView) findViewById(R.id.weel_start_time);
        weel_stopTime = (WheelView) findViewById(R.id.weel_stop_time);

        et_city = (EditText) findViewById(R.id.et_city);
        et_description = (EditText) findViewById(R.id.et_description);
        model = new SharePerferenceModel();
        model.read();

        CheckBox cb_tick = (CheckBox) findViewById(R.id.cb_tick);
        cb_tick.setChecked(model.isTickSound());
        cb_tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setTickSound(b);
            }
        });
        CheckBox cb_trigger_screen = (CheckBox) findViewById(R.id.cb_trigger_screen);
        cb_trigger_screen.setChecked(model.isTriggerScreen());
        cb_trigger_screen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setTriggerScreen(isChecked);
            }
        });

        Button btn_uninstall = (Button) findViewById(R.id.btn_uninstall);
        btn_uninstall.setOnClickListener(this);
        Button btn_about = (Button) findViewById(R.id.btn_about);
        btn_about.setOnClickListener(this);


        for (int i = 0; i < 48; i++) {
            int hours = i / 2;
            int minutes = i % 2 * 30;
            String timeString = String.format("%02d:%02d", hours, (minutes + 1));
            listTime[i] = timeString;
        }

        ArrayWheelAdapter<String> timeAdpater = new ArrayWheelAdapter<String>(this, listTime);
        weel_startTime.setViewAdapter(timeAdpater);
        ArrayWheelAdapter<String> durationAdapter = new ArrayWheelAdapter<String>(this, listTime);
        weel_stopTime.setViewAdapter(durationAdapter);
        initData();

        rg_taking_clock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {
                int checkID = radioGroup.getCheckedRadioButtonId();
                switch (checkID) {
                    case R.id.rb_halfhour:
                        model.setTypeHourPower(Constants.TALKING_HALF_AN_HOUR);
                        break;
                    case R.id.rb_hours:
                        model.setTypeHourPower(Constants.TALKING_HOURS);
                        break;
                    case R.id.rb_noreport:
                        model.setTypeHourPower(Constants.TALKING_NO_REPORT);
                        break;

                }

            }
        });
    }

    private void initData() {
        int startTimeIndex = indexOfTimeString(String.format("%02d:%02d", model.getStartHourPowerTime().getHour(), model.getStartHourPowerTime().getMinute()));
        int stopTimeIndex = indexOfTimeString(String.format("%02d:%02d", model.getStopHourPowerTime().getHour(), model.getStopHourPowerTime().getMinute()));
        weel_startTime.setCurrentItem(startTimeIndex);
        weel_stopTime.setCurrentItem(stopTimeIndex);


        switch (model.getTypeHourPower()) {
            case Constants.TALKING_HALF_AN_HOUR:
                rb_halfhour.setChecked(true);
                break;
            case Constants.TALKING_HOURS:
                rb_hours.setChecked(true);
                break;
            case Constants.TALKING_NO_REPORT:
                rb_noreport.setChecked(true);
                break;
        }
        et_city.setText(model.getCity());
        et_description.setText(model.getDescription());
    }

    @Override
    protected void onPause() {
        super.onPause();
        reportTimeConfirm();
        model.setCity(et_city.getText().toString());
        model.setDescription(et_description.getText().toString());
        model.save();
        setResult(Constants.SUCCESS_CODE);
        finish();
    }

    private int indexOfTimeString(String timeString) {
        for (int i = listTime.length - 1; i >= 0; i--) {
            if (listTime[i].equals(timeString))
                return i;
        }
        return 0;
    }

    private void reportTimeConfirm() {
        String timeStart = listTime[weel_startTime.getCurrentItem()];
        String timeStop = listTime[weel_stopTime.getCurrentItem()];

        DateModel startTimeModel = new DateModel();
        startTimeModel.setTimeString(timeStart);
        DateModel stopTimeModel = new DateModel();
        stopTimeModel.setTimeString(timeStop);
        model.setStartHourPowerTime(startTimeModel);
        model.setStopHourPowerTime(stopTimeModel);
    }


    private void uninstallActivity() {
        DevicePolicyManager policyManager;
        ComponentName componentName;
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        componentName = new ComponentName(this, ActivateAdmin.class);
        policyManager.removeActiveAdmin(componentName);
        startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + FuncUnit.getBoxPackageName())));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_uninstall:
                uninstallActivity();
                break;
            case R.id.btn_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

}
