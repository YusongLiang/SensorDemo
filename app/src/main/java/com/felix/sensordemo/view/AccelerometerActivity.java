package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;

/**
 * @author Felix
 */
public class AccelerometerActivity extends BaseActivity {

    private Toolbar toolbar;
    private SensorManager mManager;
    private Sensor mAccelerometerSensor;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    float aX = event.values[0];
                    float aY = event.values[1];
                    float aZ = event.values[2];

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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
    }
}
