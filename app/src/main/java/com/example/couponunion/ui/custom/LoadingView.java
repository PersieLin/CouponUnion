package com.example.couponunion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.couponunion.R;
import com.example.couponunion.utils.LogUtils;

/**
 * 加载控件
 */
public class LoadingView extends AppCompatImageView {
    //单次旋转的角度
    private float mDegrees = 10;
    private long delayTime = 10;
    private boolean rotateFlag = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置图片资源
        setImageResource(R.mipmap.loading);
    }

    /**
     * 开始旋转，根据视图的可见情况进行设置
     */
    public void startRotate() {
        rotateFlag = true;
        post(rotateTask());
        LogUtils.d(this, "startRotate......");

    }

    /**
     * 停止旋转
     */
    public void stopRotate() {
        rotateFlag = false;
        //停止旋转 取消回调
        boolean b = removeCallbacks(rotateTask());
        LogUtils.d(this, "isStopRotate ==>" + b);
    }

    public Runnable rotateTask() {
        return new Runnable() {
            @Override
            public void run() {
                if (mDegrees == 360) {
                    mDegrees = 0;
                }
                mDegrees = mDegrees + 10;
                //记得刷新
                invalidate();
                //当布局不可见时或者需要停止旋转时，取消callback
                if (getVisibility() != VISIBLE && !rotateFlag) {
                    removeCallbacks(this);
                } else {
                    postDelayed(rotateTask(), delayTime);
                }
            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //当控件处于可见状态时开始旋转
        LogUtils.d(this, "onAttachedToWindow ......");
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        //当控件被释放时，停止旋转
        super.onDetachedFromWindow();
        LogUtils.d(this, "onDetachedFromWindow ......");
        stopRotate();
    }

    /**
     * 重写onDraw方法，实现图片的旋转
     */
    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        //旋转
        canvas.rotate(mDegrees, width / 2, height / 2);
        //将父类的重载方法放在最后才能生效
        super.onDraw(canvas);
    }
}
