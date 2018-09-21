package com.credithc.hhr.module_home.adapter;

import android.text.TextUtils;
import android.view.View;


import com.credithc.hhr.module_home.R;
import com.credithc.hhr.module_home.view.widget.RatioImageView;


import com.credithc.hhr.library_common.bean.CardListBean;

import com.woaiqw.adapter.base.BaseSmartAdapter;
import com.woaiqw.adapter.holder.BaseViewHolder;
import com.woaiqw.base.utils.ImageLoader;

/**
 * Created by haoran on 2018/6/28.
 */

public class CardListAdapter extends BaseSmartAdapter<CardListBean.CardBean, CardListAdapter.MyViewHolder> {


    public CardListAdapter() {
        super(R.layout.home_item_card_list);
    }

    @Override
    protected void convert(MyViewHolder holder, CardListBean.CardBean cardBean) {

        int position = holder.getLayoutPosition();
        holder.iv.setImageRatio(position % 2 == 0 ? 0.7f : 0.6f);
        if (!TextUtils.isEmpty(cardBean.getImgurl()))
            // 加载图片
            ImageLoader.loadImage(holder.iv, cardBean.getImgurl());

    }

    static class MyViewHolder extends BaseViewHolder {

        private RatioImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = getView(R.id.category_iv);
        }

    }

}

