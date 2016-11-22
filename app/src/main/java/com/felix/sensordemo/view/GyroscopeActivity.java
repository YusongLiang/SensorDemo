package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;

/**
 * @author Felix
 */
public class GyroscopeActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView ivEyeRight;
    private ImageView ivEyeLeft;
    private SensorManager mManager;
    private Sensor mGyroscopeSensor;
    private float MAX_ANGULAR_SPEED = 2;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GYROSCOPE:
                    float axisX = event.values[0];
                    float axisY = event.values[1];
                    float axisZ = event.values[2];
                    if (axisZ > MAX_ANGULAR_SPEED || axisZ < -MAX_ANGULAR_SPEED) {
                        onAxisZRotate(axisZ);
                    }
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * 绕Z轴旋转时执行
     *
     * @param angularSpeed 旋转角速度（rad/s）
     */
    private void onAxisZRotate(float angularSpeed) {
        AnimationSet set = getRotateZAnimation(angularSpeed);
        ivEyeLeft.startAnimation(set);
        ivEyeRight.startAnimation(set);
    }

    /**
     * 获取绕Z轴旋转动画
     *
     * @param angularSpeed 旋转角速度（rad/s）
     * @return 旋转 {@link AnimationSet} 动画
     */
    private AnimationSet getRotateZAnimation(float angularSpeed) {
        AnimationSet set = new AnimationSet(true);
        int from;
        int to;
        if (angularSpeed > MAX_ANGULAR_SPEED) {
            from = 360;
            to = 0;
        } else {
            from = 0;
            to = 360;
        }
        RotateAnimation rotateAnimation = new RotateAnimation(
                from, to,
                Animation.RELATIVE_TO_PARENT, 0.5F,
                Animation.RELATIVE_TO_PARENT, 0.5F
        );
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1000);
        set.addAnimation(rotateAnimation);
        return set;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_gyroscope;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivEyeRight = (ImageView) findViewById(R.id.iv_eye_right);
        ivEyeLeft = (ImageView) findViewById(R.id.iv_eye_left);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscopeSensor = mManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
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
     * 注册传感器监听器
     */
    private void registerListener() {
        mManager.registerListener(mSensorEventListener, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 注销传感器监听器
     */
    private void unregisterListener() {
        mManager.unregisterListener(mSensorEventListener, mGyroscopeSensor);
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
    }
}
