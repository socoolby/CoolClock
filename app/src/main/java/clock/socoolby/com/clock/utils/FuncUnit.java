package clock.socoolby.com.clock.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import clock.socoolby.com.clock.ClockApplication;

/**
 * Alway zuo,never die.
 * Created by socoolby on 03/01/2017.
 */

public class FuncUnit {
    private final static String TAG = FuncUnit.class.getSimpleName();

    public static String getBoxPackageName() {
        try {
            PackageInfo packageInfo = ClockApplication.getContext().getPackageManager().getPackageInfo(ClockApplication.getContext().getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionName(String packageName) {
        try {
            PackageInfo packageInfo = ClockApplication.getContext().getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static void openURL(Context c, String url) {
        if (url == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        c.startActivity(intent);
    }

    /**
     * @param context
     * @param className Activity Class Name
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
