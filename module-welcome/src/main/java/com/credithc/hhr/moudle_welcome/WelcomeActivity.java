package com.credithc.hhr.moudle_welcome;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.credithc.hhr.library_common.config.ActionConstants;
import com.credithc.hhr.library_common.config.EventConstants;
import com.credithc.hhr.module_welcome.R;
import com.credithc.hhr.moudle_welcome.utils.ProcessUtils;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.base.common.PermissionActivity;
import com.woaiqw.base.utils.ActivityUtils;
import com.woaiqw.base.utils.PermissionListener;
import com.woaiqw.base.utils.ToastUtil;
import com.woaiqw.base.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;

import java.util.List;

/**
 * Created by haoran on 2018/9/7.
 */
public class WelcomeActivity extends PermissionActivity {

    private static final String TAG = "WelcomeActivity";
    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    }, Looper.getMainLooper());

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            entryMain();
        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Avatar.get().register(this);
        ActivityUtils.addActivity(this);
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

                ToastUtil.showShortToast(getString(R.string.welcome_permission_tip));

            }
        });
        h.postDelayed(task, 3000);
    }


    private void entryMain() {

        try {
            SCM.get().req(this, ActionConstants.ENTRY_MAIN_PAGE, new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Log.e(TAG, data + "：" + ActionConstants.ENTRY_MAIN_PAGE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(tag = EventConstants.FINISH_WELCOME_PAGE)
    public void releaseProcess(String data) {
        Log.e(TAG, data + "：start - - - gc");
        finish();
        ProcessUtils.killCurrentProcess(getApplication());
        Log.e(TAG, "end - - gc - - release");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
        Avatar.get().unregister(this);
        h.removeCallbacksAndMessages(null);
    }


}
