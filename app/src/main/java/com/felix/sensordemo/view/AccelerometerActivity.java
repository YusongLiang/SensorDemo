package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

/**
 * @author Felix
 */
public class AccelerometerActivity extends BaseActivity {

    private static final int REPEAT_COUNT = 8;
    private Toolbar toolbar;
    private RelativeLayout rlShake;
    private ConstraintLayout constraintLayout;
    private SensorManager mManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;
    private Handler mHandler = new Handler();
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    float aX = event.values[0];
                    float aY = event.values[1];
                    float aZ = event.values[2];
                    double aTotal = Math.sqrt(aX * aX + aY * aY + aZ * aZ);
                    if (Math.abs(aTotal) > 25) {
                        onShake(aTotal);
                    }
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_accelerometer;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rlShake = (RelativeLayout) findViewById(R.id.rl_shake);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 摇晃手机时执行的操作
     *
     * @param acceleration 总加速度
     */
    private void onShake(double acceleration) {
        Log.d(Constants.APP_TAG, "on shake");
        int duration = (int) (acceleration * 10);
        mVibrator.vibrate(duration);
        AnimationSet set = new AnimationSet(true);
        RotateAnimation animation = new RotateAnimation(
                -8, 8,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        animation.setRepeatCount(REPEAT_COUNT);
        animation.setInterpolator(new CycleInterpolator(REPEAT_COUNT));
        animation.setDuration(duration / REPEAT_COUNT);
        set.addAnimation(animation);
        rlShake.startAnimation(set);
        dropDownHearts();
    }

    /**
     * 心形下落
     */
    private void dropDownHearts() {
        for (int i = 0; i < 5; i++) {
            final ImageView imgView = new ImageView(this);
            if (Math.random() > 0.5)
                imgView.setImageResource(R.drawable.ic_shake_heart_small);
            else imgView.setImageResource(R.drawable.ic_shake_heart_small2);
            constraintLayout.addView(imgView);
            imgView.startAnimation(getDropDownAnimation());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    constraintLayout.removeView(imgView);
                }
            }, 1000);
        }
    }

    /**
     * 获取心形下落动画
     *
     * @return 下落的{@link AnimationSet}对象
     */
    private AnimationSet getDropDownAnimation() {
        float x = (float) Math.random() - 0.1f;
        float y = 1 + 5 * (float) Math.random();
        final AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, x,
                Animation.RELATIVE_TO_PARENT, x,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, y
        );
        set.setInterpolator(new AccelerateInterpolator());
        set.setFillAfter(true);
        set.setDuration(1000);
        set.addAnimation(translateAnimation);
        return set;
    }

    /**
     * 注册传感器监听器
     */
    private void registerListener() {
        mManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 注销传感器监听器
     */
    private void unregisterListener() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometerSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
        if (mVibrator != null) mVibrator.cancel();
    }
}
