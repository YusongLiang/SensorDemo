package com.felix.sensordemo.view;

import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

/**
 * @author Felix
 */
public class LightActivity extends BaseActivity {

    private static final float MAX_LIGHT = 10;
    private Toolbar toolbar;
    private TextView tvUp;
    private TextView tvDown;
    private TextView tvLightMsg;
    private SensorManager mManager;
    private Sensor mLightSensor;

    /**
     * 是否正在执行动画
     */
    private boolean isAnimating;

    /**
     * 先前是否为暗光
     */
    private boolean lastIsDark;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_LIGHT:
                    float light = event.values[0];
                    Log.d(Constants.APP_TAG, "light = " + light + " lux");
                    tvLightMsg.setText("当前光照强度：" + light + " lux");
                    onLighted(light);
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * 接收到光照数据
     *
     * @param light 光照强度（lux）
     */
    private void onLighted(float light) {
        if (!isAnimating) {
            final boolean isDark = light <= MAX_LIGHT;
            if (lastIsDark != isDark) {
                isAnimating = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastIsDark = isDark;
                        isAnimating = false;
                    }
                }, 1000);
                startAnimation(isDark);
            }
        }
    }

    /**
     * 开始执行眼皮动画
     *
     * @param isDark 是否为暗光
     */
    private void startAnimation(boolean isDark) {
        float from;
        float to;
        if (isDark) {
            from = 0;
            to = 100;
        } else {
            from = 100;
            to = 0;
        }
        ObjectAnimator animatorUp = ObjectAnimator.ofFloat(tvUp, "translationY", from, to);
        animatorUp.setDuration(900);
        animatorUp.start();
        ObjectAnimator animatorDown = ObjectAnimator.ofFloat(tvDown, "translationY", -from, -to);
        animatorDown.setDuration(900);
        animatorDown.start();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_light;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvUp = (TextView) findViewById(R.id.tv_up);
        tvDown = (TextView) findViewById(R.id.tv_down);
        tvLightMsg = (TextView) findViewById(R.id.tv_light_msg);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLightSensor = mManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(LightActivity.this);
            }
        });
    }

    private void registerListener() {
        mManager.registerListener(mSensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterListener() {
        mManager.unregisterListener(mSensorEventListener, mLightSensor);
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