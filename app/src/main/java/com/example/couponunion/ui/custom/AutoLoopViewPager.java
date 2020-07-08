package com.example.couponunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.couponunion.R;


/**
 * 实现自动轮播的ViewPager
 */
public class AutoLoopViewPager extends ViewPager {

    private boolean loopFlag = false;
    private static final long DEFAULT_DURATION = 3000;
    private long mDuration;

    public AutoLoopViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoLoopViewPager);
        mDuration = (long) ta.getFloat(R.styleable.AutoLoopViewPager_duration, DEFAULT_DURATION);
        //记得释放资源
        ta.recycle();
    }



    public void startLoop(){
        //向外提供方法启动自动轮播
        loopFlag = true;
        post(loopTask());
    }

    public void stopLoop(){
        //停止轮播
        loopFlag = false;
        removeCallbacks(loopTask());
    }

    /**
     * 设置轮播时长
     * @param duration  轮播时长，单位毫秒
     */
    public void setDuration(int duration){
        this.mDuration = duration;
    }

    private Runnable loopTask(){
        return () -> {
            if (loopFlag){
                int currentItem = getCurrentItem();
                currentItem ++;
                setCurrentItem(currentItem);
//                LogUtil.d(AutoLoopViewPager.this, "looping....." + " delaytime -->" + delayTime);
                postDelayed(loopTask(), mDuration);
            }

        };
    }

}
