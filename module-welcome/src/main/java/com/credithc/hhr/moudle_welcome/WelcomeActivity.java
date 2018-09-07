package com.credithc.hhr.moudle_welcome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.credithc.hhr.library_common.config.ActionConstants;
import com.credithc.hhr.module_welcome.R;
import com.woaiqw.base.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/9/7.
 */
public class WelcomeActivity extends AppCompatActivity {

    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            entryMain();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity_welcome);

        h.postDelayed(task, 3000);

    }

    private void entryMain() {

        try {
            SCM.get().req(this, ActionConstants.ENTRY_MAIN_PAGE, new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Toast.makeText(WelcomeActivity.this, data, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacksAndMessages(null);
    }
}
