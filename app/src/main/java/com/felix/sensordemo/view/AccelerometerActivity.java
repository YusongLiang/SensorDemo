package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;

/**
 * @author Felix
 */
public class AccelerometerActivity extends BaseActivity {

    private static final int REPEAT_COUNT = 8;
    private Toolbar toolbar;
    private TextView tvShake;
    private SensorManager mManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;
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
                        onShack(aTotal);
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
        tvShake = (TextView) findViewById(R.id.tv_shake);
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
    private void onShack(double acceleration) {
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
        tvShake.startAnimation(set);
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
