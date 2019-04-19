package com.wuruoye.know.util.base;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Created by wuruoye on 2017/11/20.
 * this file is to be the config class of the app
 */

public class WConfig {
    private static Handler sMainHandler;
    private static Context sAppContext;

    public static void init(@NotNull Context context) {
        String packageName = context.getPackageName();
        APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + packageName + "/";
        FILE_PATH = APP_PATH + "file/";
        IMAGE_PATH = APP_PATH + "image/";
        VIDEO_PATH = APP_PATH + "video/";
        RECORD_PATH = APP_PATH + "record/";
        PROVIDER_AUTHORITY = packageName + ".fileprovider";

        sMainHandler = new Handler(Looper.getMainLooper());
        sAppContext = context;
    }

    public static void runOnUIThread(Runnable runnable) {
        if (sMainHandler != null) {
            sMainHandler.post(runnable);
        }
    }

    @Contract(pure = true)
    public static Context getAppContext() {
        return sAppContext;
    }

    public static String APP_PATH;
    public static String FILE_PATH;
    public static String IMAGE_PATH;
    public static String VIDEO_PATH;
    public static String RECORD_PATH;
    public static String PROVIDER_AUTHORITY;

    // permission related
    public static final String[] FILE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA
    };

    public static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] AUDIO_PERMISSION = {
            Manifest.permission.RECORD_AUDIO
    };

    public static final int CODE_CHOOSE_PHOTO = 1<<15-1;
    public static final int CODE_TAKE_PHOTO = 1<<15-2;
    public static final int CODE_CROP_PHOTO = 1<<15-3;

    public static final int CODE_PERMISSION_FILE = 1<<15-1;
    public static final int CODE_PERMISSION_CAMERA = 1<<15-2;
}
