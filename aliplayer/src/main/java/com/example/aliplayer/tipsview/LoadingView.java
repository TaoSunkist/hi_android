package com.example.aliplayer.tipsview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aliplayer.R;
/**
 * 加载提示对话框。加载过程中，缓冲过程中会显示。
 */
public class LoadingView extends RelativeLayout {
    private static final String TAG = LoadingView.class.getSimpleName();
    private TextView mLoadPercentView;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_alivc_loading, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, params);

        mLoadPercentView = (TextView) view.findViewById(R.id.net_speed);
        mLoadPercentView.setText("视频正在加载中" + " 0%");
    }

    /**
     * 更新加载进度
     *
     * @param percent 百分比
     */
    public void updateLoadingPercent(int percent) {
        mLoadPercentView.setText("视频正在加载中" + percent + "%");
    }

    /**
     * 只显示loading，不显示进度提示
     */
    public void setOnlyLoading() {
        findViewById(R.id.loading_layout).setVisibility(GONE);
    }

}
