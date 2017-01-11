package clock.socoolby.com.clock.utils;


import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import clock.socoolby.com.clock.ClockApplication;

/**
 * Alway zuo,never die.
 * Created by socoolby on 12/11/15.
 */
public class FileUtils {


    public static boolean isExistsFile(String fileName) {
        File file = getFile(fileName);
        return file.exists();
    }

    public static File getFile(String path) {
        try {
            return new File(ClockApplication.getContext().getFilesDir() + path);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void writeObject(String key, Object obj) {
        if (obj == null || key == null) {
            return;

        }
        try {
            FileOutputStream out = new FileOutputStream(getFile(key));
            ObjectOutputStream outObj = new ObjectOutputStream(out);
            outObj.writeObject(obj);
            outObj.flush();
            outObj.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(String key) {
        try {
            FileInputStream in = new FileInputStream(getFile(key));
            ObjectInputStream inObj = new ObjectInputStream(in);
            Object obj = inObj.readObject();
            inObj.close();
            in.close();
            return obj;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        File file = getFile(key);
        file.delete();
        ClockApplication.getInstance().init();
        return readObject(key);

    }


}
