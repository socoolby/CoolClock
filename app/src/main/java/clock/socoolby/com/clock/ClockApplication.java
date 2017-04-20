package clock.socoolby.com.clock;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import clock.socoolby.com.clock.protocol.BusinessService;
import clock.socoolby.com.clock.utils.Constants;
import clock.socoolby.com.clock.utils.DateModel;
import clock.socoolby.com.clock.utils.FileUtils;
import clock.socoolby.com.clock.utils.SharePerferenceModel;


public class ClockApplication extends Application {

    private static ClockApplication sEndzoneBoxApp;
    private BusinessService mBusinessService = new BusinessService();

    public static ClockApplication getInstance() {
        return sEndzoneBoxApp;
    }

    @Override
    public void onCreate() {
        sEndzoneBoxApp = this;
        init();
        super.onCreate();
    }


    public void init() {
        if (!FileUtils.isExistsFile(Constants.SHARE_PERFERENCE_FILE)) {
            SharePerferenceModel model = new SharePerferenceModel();
            model.setTypeHourPower(Constants.TALKING_HALF_AN_HOUR);
            DateModel startTimeModel = new DateModel();
            startTimeModel.setTime(12, 31);
            DateModel stopTimeModel = new DateModel();
            stopTimeModel.setTime(14, 31);
            model.setStartHourPowerTime(startTimeModel);
            model.setStopHourPowerTime(stopTimeModel);
            model.setCity(getString(R.string.shenzhen));
            model.save();
        }
    }

    public static Context getContext() {
        return ClockApplication.getInstance().getApplicationContext();
    }

    public BusinessService getBusinessService() {
        return mBusinessService;
    }

    private MainActivity mMainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mMainActivity;
    }

}
