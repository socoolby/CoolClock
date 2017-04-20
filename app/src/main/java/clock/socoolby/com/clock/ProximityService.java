package clock.socoolby.com.clock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import clock.socoolby.com.clock.utils.FuncUnit;
import clock.socoolby.com.clock.utils.ScreenManager;

/**
 * Alway zuo,never die.
 * Created by socoolby on 16/04/2017.
 */

public class ProximityService extends Service {
    private final static String TAG = ProximityService.class.getSimpleName();

    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] its = sensorEvent.values;
            Log.d(TAG, String.format("its %f %f %f len:,%d", its[0], its[1], its[2], its.length));
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (FuncUnit.isForeground(ClockApplication.getContext(), MainActivity.class.getName())) {
                    if (its[0] == 0.0) {
                        System.out.println("Hand stay");
                        if (ScreenManager.isScreenOn()) {
                            ScreenManager.systemLock(ClockApplication.getInstance().getMainActivity());
                        } else {
                            ScreenManager.systemUnLock();
                        }
                    } else {
                        System.out.println("Hand leave...");
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onDestroy();
    }
}
