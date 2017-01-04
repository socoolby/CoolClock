package clock.socoolby.com.clock.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import clock.socoolby.com.clock.ClockApplication;
import clock.socoolby.com.clock.ActivateAdmin;

public class ScreenManager {
    public static boolean isScreenOn() {
        PowerManager pm = (PowerManager) ClockApplication.getContext().getSystemService(Activity.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public static void turnScreenOff() {
        PowerManager pm = (PowerManager) ClockApplication.getContext().getSystemService(Activity.POWER_SERVICE);
        pm.goToSleep(SystemClock.uptimeMillis());
    }

    public static void systemLock(Activity context) {
        DevicePolicyManager policyManager;
        ComponentName componentName;
        policyManager = (DevicePolicyManager) context.getSystemService(Activity.DEVICE_POLICY_SERVICE);

        componentName = new ComponentName(context, ActivateAdmin.class);

        if (!policyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            context.startActivity(intent);
        }
        if (policyManager.isAdminActive(componentName)) {
            Window localWindow = context.getWindow();
            WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
            localLayoutParams.screenBrightness = 1.0F;
            localWindow.setAttributes(localLayoutParams);
            policyManager.lockNow();
        }

    }

    public static void systemUnLock() {
        PowerManager pm = (PowerManager) ClockApplication.getContext().getSystemService(Activity.POWER_SERVICE);
        PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "SimpleTimer");
        mWakelock.acquire();
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
