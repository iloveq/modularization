package com.credithc.hhr.module_main.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Created by yhr on 2017/11/13.
 */

public class TabView extends android.support.v7.widget.AppCompatTextView implements Checkable {

    private boolean checked;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public TabView(Context context) {
        super(context);
    }


    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }



    @Override
    public boolean isChecked() {
        return checked;
    }


    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshDrawableState();
    }


    @Override
    public void toggle() {
        this.checked = !checked;
    }

}
