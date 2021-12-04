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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.kiotsdk.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;


/**
 * 选择照片底部弹窗
 */
public class SeekBarBottomDialog extends Dialog {

    TextView mTvName;
    TextView mTvSubName;
    TextView mTvCurrProgress;
    IndicatorSeekBar mSeekBar;
    TextView mTvMinProgress;
    TextView mTvMaxProgress;

    private Context mContext;

    private int mSelectProgress = 0;

    public SeekBarBottomDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_seek_bar_bottom);
        initView();
    }

    private void initView() {

        mTvName = findViewById(R.id.dialog_title_name);
        mTvSubName = findViewById(R.id.dialog_tv_sub_name);
        mTvCurrProgress = findViewById(R.id.dialog_tv_curr_progress);
        mSeekBar = findViewById(R.id.dialog_seek_bar);
        mTvMinProgress = findViewById(R.id.dialog_tv_min_progress);
        mTvMaxProgress = findViewById(R.id.dialog_tv_max_progress);

        findViewById(R.id.dialog_btn_login).setOnClickListener(v -> {
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.onFinishClick(mSelectProgress);
            }
            dismiss();
        });

        mSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                mSelectProgress = seekParams.progress;
                mTvCurrProgress.setText("" + mSelectProgress);
//                Log.i(TAG, seekParams.seekBar);
//                Log.i(TAG, seekParams.progress);
//                Log.i(TAG, seekParams.progressFloat);
//                Log.i(TAG, seekParams.fromUser);
//                //when tick count > 0
//                Log.i(TAG, seekParams.thumbPosition);
//                Log.i(TAG, seekParams.tickText);
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

}
