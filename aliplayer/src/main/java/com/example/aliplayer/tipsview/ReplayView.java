package com.example.aliplayer.tipsview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aliplayer.AliyunVodPlayerView;
import com.example.aliplayer.R;
import com.example.aliplayer.theme.ITheme;

/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

/**
 * 重播提示对话框。播放结束的时候会显示这个界面
 */
public class ReplayView extends RelativeLayout implements ITheme {
    //重播按钮
    private TextView mReplayBtn;
    //重播事件监听
    private OnReplayClickListener mOnReplayClickListener = null;

    public ReplayView(Context context) {
        super(context);
        init();
    }

    public ReplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_alivc_replay, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, params);

        //设置监听
        mReplayBtn = view.findViewById(R.id.replay);
        mReplayBtn.setOnClickListener(v -> {
            if (mOnReplayClickListener != null) {
                mOnReplayClickListener.onReplay();
            }
        });
    }

    @Override
    public void setTheme(AliyunVodPlayerView.Theme theme) {
    }

    /**
     * 重播点击事件
     */
    public interface OnReplayClickListener {
        /**
         * 重播事件
         */
        void onReplay();
    }

    /**
     * 设置重播事件监听
     *
     * @param l 重播事件
     */
    public void setOnReplayClickListener(OnReplayClickListener l) {
        mOnReplayClickListener = l;
    }

}
