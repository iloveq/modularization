package com.credithc.hhr.moudle_welcome.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.woaiqw.avatar.utils.ProcessUtil;

/**
 * Created by haoran on 2018/9/11.
 */
public class ProcessUtils {
    public static void killCurrentProcess(Application a) {
        ActivityManager activityManager = (ActivityManager) a.getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        String currentProcessName = ProcessUtil.getCurrentProcessName(a);
        Log.e("Process will be kill：", currentProcessName);
        activityManager.killBackgroundProcesses(currentProcessName);
    }
}
