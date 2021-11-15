package com.example.kiotsdk.widget.roundprogress;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by LianCP on 2018/3/21.
 */
public class RateTextPercentageRing extends FrameLayout implements PercentageRing.OnProgressChangeListener {
    private PercentageRing mCircularProgressBar;
    private TextView mRateText;
    private String unit;

    public RateTextPercentageRing(Context context) {
        super(context);
        init();
    }

    public RateTextPercentageRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCircularProgressBar = new PercentageRing(getContext());
        this.addView(mCircularProgressBar);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        mCircularProgressBar.setLayoutParams(lp);

        mRateText = new TextView(getContext());
        this.addView(mRateText);
        mRateText.setLayoutParams(lp);
        mRateText.setGravity(Gravity.CENTER);
        mRateText.setTextColor(Color.parseColor("#000000"));
        mRateText.setTextSize(25);
        mRateText.setText("0s");

        mCircularProgressBar.setOnProgressChangeListener(this);
    }

    /**
     * 设置最大值
     */
    public void setMax(int max) {
        mCircularProgressBar.setMax(max);
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        mCircularProgressBar.setProgress(progress);
    }

    /**
     * 得到 CircularProgressBar 对象，用来设置其他的一些属性
     *
     * @return
     */
    public PercentageRing getCircularProgressBar() {
        return mCircularProgressBar;
    }

    /**
     * 设置中间进度百分比文字的尺寸
     *
     * @param size
     */
    public void setTextSize(float size) {
        mRateText.setTextSize(size);
    }

    /**
     * 设置中间进度百分比文字的颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mRateText.setTextColor(color);
    }


    /**
     * 设置进度条进度的颜色
     */
    public void setPrimaryColor(int color) {
        mCircularProgressBar.setPrimaryColor(color);
    }

    @Override
    public void onChange(int duration, int progress, float rate) {

        if (unit == null) {
            mRateText.setText(String.valueOf((int) (rate * duration) + "s"));
        } else {
            mRateText.setText(String.valueOf((int) (rate * duration) + unit));
        }
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getProgress() {

        return mCircularProgressBar.getProgress();
    }
    public void setmRateText(String text){


        mRateText.setText(text);
    }

}
