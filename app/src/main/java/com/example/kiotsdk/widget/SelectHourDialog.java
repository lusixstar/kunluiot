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
public class SelectHourDialog extends Dialog {

    TextView mTvTitleName;
    PickerView mPVStartHour;
    PickerView mPVEndHour;
    private Context mContext;

    private String mStartHour = "00:00";
    private String mEndHour = "00:00";

    private ArrayList<String> mStartHours = new ArrayList<String>();
    private ArrayList<String> mEndHours = new ArrayList<String>();

    public SelectHourDialog(@NonNull Context context) {
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
        mPVStartHour = findViewById(R.id.pv_minute);
        mPVEndHour = findViewById(R.id.pv_second);

        findViewById(R.id.dialog_btn_cancel).setOnClickListener(v -> dismiss());
        findViewById(R.id.dialog_btn_sure).setOnClickListener(v -> {
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.onFinishClick(mStartHour, mEndHour);
            }
            dismiss();
        });

        for (int i = 0; i < 24; i++) {
            String str = String.format("%02d", i);
            mStartHours.add(str+":00");
            mEndHours.add(str+":00");
        }

        mPVStartHour.setData(mStartHours);
        mPVStartHour.setSelected(mStartHour);
        mPVStartHour.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                mStartHour = text;
            }
        });
        mPVEndHour.setData(mEndHours);
        mPVEndHour.setSelected(mEndHour);
        mPVEndHour.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                mEndHour = text;
            }
        });

    }

    public void setStartHourData(ArrayList<String> minutes) {
        mStartHours.clear();
        mStartHours.addAll(minutes);
        mPVStartHour.setData(mStartHours);
    }

    public void setEndHourData(ArrayList<String> seconds) {
        mEndHours.clear();
        mEndHours.addAll(seconds);
        mPVEndHour.setData(mEndHours);
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

    public void setStartHour(String startHour) {
        this.mStartHour = startHour;
        mPVStartHour.setSelected(startHour);
    }

    public void setEndHour(String endHour) {
        this.mEndHour = endHour;
        mPVEndHour.setSelected(mEndHour);
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
        void onFinishClick(String startHour, String endHour);
        void onDismissClick();
    }
}
