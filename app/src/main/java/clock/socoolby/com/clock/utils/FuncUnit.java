package clock.socoolby.com.clock.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import clock.socoolby.com.clock.ClockApplication;

/**
 * Alway zuo,never die.
 * Created by socoolby on 03/01/2017.
 */

public class FuncUnit {
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

}
