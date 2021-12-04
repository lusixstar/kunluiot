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

import java.util.ArrayList;


/**
 * 选择时间底部弹窗
 */
public class SelectTimeDialog extends Dialog {

    TextView mTvTitleName;
    PickerView mPVMinute;
    PickerView mPVSecond;
    private Context mContext;

    private String mMinute = "00";
    private String mSecond = "00";

    private ArrayList<String> mMinutes = new ArrayList<String>();
    private ArrayList<String> mSeconds = new ArrayList<String>();

    public SelectTimeDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_time);
        initViews();
    }

    private void initViews() {

        mTvTitleName = findViewById(R.id.dialog_title_name);
        mPVMinute = findViewById(R.id.pv_minute);
        mPVSecond = findViewById(R.id.pv_second);

        findViewById(R.id.dialog_btn_cancel).setOnClickListener(v -> dismiss());
        findViewById(R.id.dialog_btn_sure).setOnClickListener(v -> {
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.onFinishClick(mMinute, mSecond);
            }
            dismiss();
        });

        for (int i = 0; i < 60; i++) {
            mMinutes.add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            mSeconds.add(i < 10 ? "0" + i : "" + i);
        }
        mPVMinute.setData(mMinutes);
        mPVMinute.setSelected(mMinute);
        mPVMinute.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                mMinute = text;
            }
        });
        mPVSecond.setData(mSeconds);
        mPVSecond.setSelected(mSecond);
        mPVSecond.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                mSecond = text;
            }
        });

    }

    public void setMinuteData(ArrayList<String> minutes) {
        mMinutes.clear();
        mMinutes.addAll(minutes);
        mPVMinute.setData(mMinutes);
    }

    public void setSecondData(ArrayList<String> seconds) {
        mSeconds.clear();
        mSeconds.addAll(seconds);
        mPVSecond.setData(mSeconds);
    }

    /**
     * 设置Dialog标题
     *
     * @param text
     * @return
     */
    public void setTitleName(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mTvTitleName.setVisibility(View.GONE);
            return;
        }
        mTvTitleName.setVisibility(View.VISIBLE);
        mTvTitleName.setText(text);
    }

    public void setMinute(String minute) {
        this.mMinute = minute;
        mPVMinute.setSelected(mMinute);
    }

    public void setSecond(String second) {
        this.mSecond = second;
        mPVSecond.setSelected(mSecond);
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

//    @OnClick({R.id.dialog_btn_cancel, R.id.dialog_btn_sure})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.dialog_btn_cancel:
//                break;
//            case R.id.dialog_btn_sure:
//                if (mOnDialogClickListener != null) {
//                    mOnDialogClickListener.onFinishClick(mMinute, mSecond);
//                }
//                break;
//
//            default:
//                break;
//        }
//        dismiss();
//    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnDialogClickListener != null) {
            mOnDialogClickListener.onDismissClick();
        }
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

    /**
     * 按钮点击监听
     */
    public interface OnDialogClickListener {
        void onFinishClick(String minute, String second);

        void onDismissClick();
    }
}
