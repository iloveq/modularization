package com.woaiqw.sdk_share.view;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.woaiqw.sdk_share.R;
import com.woaiqw.sdk_share.ShareChannel;

/**
 * Created by haoran on 2018/8/6.
 */

public class ShareDialog extends DialogFragment implements IShareView, View.OnClickListener {

    private TextView tvShareCancel;
    private RecyclerView mRecyclerView;

    private static final String KEY = "share_dialog";
    private OnShareClickListener listener;

    @Override
    public void setOnShareClickListener(OnShareClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_bottom_share);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        initViewAndClick(dialog, getArguments().getIntArray(KEY));
        return dialog;
    }

    private void initViewAndClick(Dialog view, int[] arr) {
        mRecyclerView = view.findViewById(R.id.recycler_view_share);
        tvShareCancel = view.findViewById(R.id.tv_share_cancel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new ShareAdapter(arr));
        tvShareCancel.setOnClickListener(this);
    }

    @Override
    public IShareView createShareDialog(int[] shareChannel) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(KEY, shareChannel);
        ShareDialog dialog = new ShareDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int show(FragmentTransaction transaction) {
        return super.show(transaction, KEY);
    }

    @Override
    public void show(FragmentManager manager) {
        super.show(manager, KEY);
    }

    @Override
    public void onClick(View v) {
        dismissDialog();
    }

    @Override
    public void dismissDialog() {
        listener.onShareClick(ShareChannel.CHANNEL_CANNEL);
        dismissAllowingStateLoss();
    }


    private class ShareAdapter extends RecyclerView.Adapter<MyShareHolder> {

        private int[] resId;
        private String[] name;

        ShareAdapter(int[] arr) {
            resId = new int[arr.length];
            name = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == ShareChannel.CHANNEL_WECHAT) {
                    resId[i] = R.drawable.share_wx;
                    name[i] = "微信";
                } else if (arr[i] == ShareChannel.CHANNEL_WECHAT_MOMENT) {
                    resId[i] = R.drawable.share_wx_moment;
                    name[i] = "朋友圈";
                } else if (arr[i] == ShareChannel.CHANNEL_QQ) {
                    resId[i] = R.drawable.share_qq;
                    name[i] = "QQ";
                } else if (arr[i] == ShareChannel.CHANNEL_QQ_ZONE) {
                    resId[i] = R.drawable.share_qq_zone;
                    name[i] = "QQ空间";
                } else if (arr[i] == ShareChannel.CHANNEL_WEIBO) {
                    resId[i] = R.drawable.share_sine;
                    name[i] = "微博";
                } else if (arr[i] == ShareChannel.CHANNEL_MORE) {
                    resId[i] = R.drawable.share_more;
                    name[i] = "更多";
                } else {
                    throw new RuntimeException(" ShareChannel 数组初始化错误 ");
                }
            }
        }

        @Override
        public MyShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyShareHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
        }

        @Override
        public void onBindViewHolder(MyShareHolder holder, int position) {
            final int pos = holder.getAdapterPosition();
            holder.tvName.setText(name[pos]);
            Drawable drawable = getContext().getResources().getDrawable(resId[pos]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvName.setCompoundDrawables(null, drawable, null, null);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShareClick(pos);
                }
            });
        }

        @Override
        public int getItemCount() {
            return resId.length;
        }

    }

    private static class MyShareHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        MyShareHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_share);
        }
    }
}
