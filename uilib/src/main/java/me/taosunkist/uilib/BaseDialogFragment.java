package me.taosunkist.uilib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class BaseDialogFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {
    /**
     * group的进入动画和退出动画
     */
    private Animation mCoverInAnim, mCoverOutAnim;
    /**
     * body的进入动画和退出动画
     */
    private Animation mBodyInAnim, mBodyOutAnim;

    static public BaseDialogFragment newInstance() {
        BaseDialogFragment baseDialogFragment = new BaseDialogFragment();
        return baseDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        loadAnimation();
        super.onCreate(savedInstanceState);
    }


    private RelativeLayout mGround;
    private RelativeLayout mBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_dialog, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(getView());
        executeInAnim();
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void findViewById(View view) {
        mGround = (RelativeLayout) view.findViewById(R.id.group);
        mBody = (RelativeLayout) view.findViewById(R.id.body);
        mBody.addView(getBodyView(mBody));
        mBody.setOnClickListener(this);
        getView().setOnKeyListener(this);
    }

    private void loadAnimation() {
        mCoverInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_normal_fade_in);
        mBodyInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_body_normal_slide_in);
        mCoverOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_normal_fade_out);
        mBodyOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_body_normal_slide_out);
    }

    /**
     * 打开的动画
     */
    private void executeInAnim() {
        mCoverInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBody.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBody.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mGround.startAnimation(mCoverInAnim);
        mBody.startAnimation(mBodyInAnim);
    }

    /**
     * 关闭的动画
     */
    private void executeOutAnim() {
        mCoverOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBody.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mGround.startAnimation(mCoverOutAnim);
        mBody.startAnimation(mBodyOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.body) {
            executeOutAnim();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            executeOutAnim();
            return true;
        }
        return false;
    }

    private View getBodyView(RelativeLayout relativeGroup) {
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeGroup.setLayoutParams(relativeParams);

        TextView sample = new TextView(getContext());
        sample.setPadding(80, 80, 80, 80);
        sample.setText("Please override getBodyView()");
        sample.setTextColor(Color.YELLOW);
        sample.setTextSize(24);
        sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });
        return sample;
    }
}
