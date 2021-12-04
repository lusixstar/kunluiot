package com.example.kiotsdk.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.kiotsdk.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.jetbrains.anko.ToastsKt;


/**
 * 窗帘电机底部控制弹框
 */
public class SeekBarCurtainsBottomDialog extends Dialog {

    TextView mTvName;
    TextView mTvSubName;
    TextView mTvCurrProgress;
    IndicatorSeekBar mSeekBar;
    TextView mTvMinProgress;
    TextView mTvMaxProgress;
    RadioButton rdGt;
    RadioButton rdGe;
    RadioButton rdBetween;
    IndicatorSeekBar dialogSeekBarMax;
    IndicatorSeekBar dialogSeekBarMini;
    LinearLayout pbBetweenParent;
    Button dialogBtnLogin;
    TextView barMaxProgress;
    TextView barMiniProgress;
    TextView tvMax;
    private Context mContext;

    private int mSelectProgress = 0;

    public SeekBarCurtainsBottomDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_curtains_seek_bar_bottom);
        initView();


    }

    private void initView() {
        mTvName = findViewById(R.id.dialog_title_name);
        mTvSubName = findViewById(R.id.dialog_tv_sub_name);
        mTvCurrProgress = findViewById(R.id.dialog_tv_curr_progress);
        mSeekBar = findViewById(R.id.dialog_seek_bar);
        mTvMinProgress = findViewById(R.id.dialog_tv_min_progress);
        mTvMaxProgress = findViewById(R.id.dialog_tv_max_progress);
        rdGt = findViewById(R.id.rd_gt);
        rdGe = findViewById(R.id.rd_ge);
        rdBetween = findViewById(R.id.rd_between);
        dialogSeekBarMax = findViewById(R.id.dialog_seek_bar_max);
        dialogSeekBarMini = findViewById(R.id.dialog_seek_bar_mini);
        pbBetweenParent = findViewById(R.id.pb_between_parent);
        dialogBtnLogin = findViewById(R.id.dialog_btn_login);
        barMaxProgress = findViewById(R.id.bar_max_progress);
        barMiniProgress = findViewById(R.id.bar_mini_progress);
        tvMax = findViewById(R.id.dialog_tv_up_max_progress);

        findViewById(R.id.dialog_btn_login).setOnClickListener(v -> {
            if (rdBetween.isChecked()) {

                if (dialogSeekBarMax.getProgress() <= dialogSeekBarMini.getProgress()) {
                    ToastsKt.toast(mContext, mContext.getString(R.string.the_upper_limit_of_interval_cannot_be_greater_than_the_lower_limit));
                    return;
                }
            }
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.onFinishClick(mSelectProgress);
            }
            dismiss();
        });

        rdGt.setChecked(true);
        rdBetween.setOnCheckedChangeListener((button, isChecked) -> {

            if (isChecked) {
                pbBetweenParent.setVisibility(View.VISIBLE);
                mSeekBar.setVisibility(View.GONE);
                mTvCurrProgress.setVisibility(View.GONE);
            } else {
                pbBetweenParent.setVisibility(View.GONE);
                mSeekBar.setVisibility(View.VISIBLE);
                mTvCurrProgress.setVisibility(View.VISIBLE);
            }

        });
        mSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                mSelectProgress = seekParams.progress;
                mTvCurrProgress.setText("" + mSelectProgress);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        dialogSeekBarMax.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

                barMaxProgress.setText(seekParams.progress + "");
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        dialogSeekBarMini.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                barMiniProgress.setText(seekParams.progress + "");
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

    }

    /**
     * 设置Dialog标题
     *
     * @param text
     * @return
     */
    public void setTitleName(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mTvName.setVisibility(View.GONE);
            return;
        }
        mTvName.setVisibility(View.VISIBLE);
        mTvName.setText(text);
    }

    /**
     * 设置Dialog二级标题
     *
     * @param text
     * @return
     */
    public void setTitleSubName(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mTvSubName.setVisibility(View.GONE);
            return;
        }
        mTvSubName.setVisibility(View.VISIBLE);
        mTvSubName.setText(text);
    }


    /**
     * @param progress
     * @return
     */
    public void setCurrProgress(int progress) {
        mSeekBar.setProgress(progress);
        mTvCurrProgress.setVisibility(View.VISIBLE);
        mTvCurrProgress.setText("" + progress);
    }


    /**
     * @param min
     * @return
     */
    public void setMinProgress(int min) {
        mSeekBar.setMin(min);
        mTvMinProgress.setVisibility(View.VISIBLE);
        mTvMinProgress.setText("" + min);
    }


    /**
     * @param max
     * @return
     */
    public void setMaxProgress(int max) {
        mSeekBar.setMax(max);
        mTvMaxProgress.setVisibility(View.VISIBLE);
        mTvMaxProgress.setText("" + max);
        dialogSeekBarMax.setMax(max);
        dialogSeekBarMini.setMax(max);
        tvMax.setText("" + max);
    }

    public void show() {
        super.show();
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);

        wl.x = 0;
        wl.y = 0;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(wl);
        // 设置点击外围隐藏
        setCanceledOnTouchOutside(true);
    }

    private OnDialogClickListener mOnDialogClickListener = null;

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.mOnDialogClickListener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnDialogClickListener != null) {
            mOnDialogClickListener.onDismissClick();
        }
    }

    /**
     * 按钮点击监听
     */
    public interface OnDialogClickListener {
        void onFinishClick(int selectProgress);

        void onDismissClick();
    }

    public int getCheckMode() {

        if (rdGe.isChecked()) {

            return 0;
        } else if (rdGt.isChecked()) {

            return 1;
        } else {
            return 2;
        }

    }

    public int getMax() {


        return dialogSeekBarMax.getProgress();
    }

    public int getMin() {

        return dialogSeekBarMini.getProgress();
    }
}
